/*
 *    Copyright 2019 Danny Lamarti
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.lamart.livedata.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.map

/**
 * The compose argument is a function that contains a custom operator.
 */

fun <T, R> LiveData<T>.compose(compose: Compose<T, R>): LiveData<R> = compose(this@compose)

/**
 * Simple way for creating a custom operator call the subscriber arguemnt whener you want to emit a value to the next LiveData.
 */

fun <T, R> LiveData<T>.wrap(wrap: (value: T, subscriber: Subscriber<R>) -> Unit): LiveData<R> =
        mediate { addSource, setValue ->
            addSource { value ->
                wrap(value, setValue)
            }
        }

/**
 * Advanced way for creating a custom operator. As opposed to `wrap`, it allows holding (mutable) state.
 */

fun <T, R> LiveData<T>.lift(lift: (subscriber: Subscriber<R>) -> Subscriber<T>): LiveData<R> =
        mediate { addSource, setValue ->
            lift(setValue).let(addSource)
        }

/**
 * Whenever the predicate returns `true`, the value will be emitted.
 */

fun <T> LiveData<T>.filter(predicate: (T) -> Boolean): LiveData<T> =
        wrap { value, next ->
            value.takeIf(predicate)?.let(next)
        }

/**
 * Cast the incoming value to the type. CAUTION: this is unsafe
 */

@Suppress("UNCHECKED_CAST")
inline fun <reified R> LiveData<*>.cast(): LiveData<R> = map { it as R }

/**
 * Safely cast the income value to the type. If it is not able to cast, it will not emit the value.
 */

inline fun <reified R> LiveData<*>.castIf(): LiveData<R> = filter { it is R }.cast()

/**
 * Only emits the first icoming value.
 */

fun <T> LiveData<T>.first(): LiveData<T> = take(1)

/**
 * Limits its emissions by the given argument.
 */

fun <T> LiveData<T>.take(count: Int): LiveData<T> =
        lift { next ->
            var total = 0

            { value ->
                if (total < count) {
                    total++
                    next(value)
                }
            }
        }

/**
 * Skips the `count` amount before emitting values.
 */

fun <T> LiveData<T>.skip(count: Int): LiveData<T> =
        lift { next ->
            var total = 0

            { value ->
                if (total < count) {
                    total++
                } else {
                    next(value)
                }
            }
        }

/**
 * Uses the previously emitted value and the current value to create a new value. The new value will be cached for the next emission.
 */

@Suppress("UNCHECKED_CAST")
fun <T> LiveData<T>.reduce(accumulator: Accumulator<T, T>): LiveData<T> =
        lift { next ->
            var seed: Any? = Uninitialized

            { value ->
                val result = when (seed) {
                    Uninitialized -> value
                    else -> accumulator(seed as T, value)
                }

                seed = result
                next(result)
            }
        }

/**
 * Uses the previously emitted value and the current value to create a new value. The new value will be cached for the next emission.
 *
 * The seed is functioning as the first `previous` value.
 */

@Suppress("UNCHECKED_CAST")
fun <T, R> LiveData<T>.reduce(seed: R, accumulator: Accumulator<T, R>): LiveData<R> =
        lift { next ->
            var cache: Any? = Uninitialized

            { value ->
                if (cache is Uninitialized) {
                    cache = seed
                    next(cache as R)
                }

                cache = accumulator(cache as R, value)
                next(cache as R)
            }
        }

/**
 * Whenever observing starts, it is prepended with the given argument.
 */

fun <T> LiveData<T>.startWith(value: T): LiveData<T> =
        lift { next ->
            next(value);
            { value ->
                next(value)
            }
        }