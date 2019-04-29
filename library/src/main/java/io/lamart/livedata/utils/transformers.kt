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

//  reduce

fun <T, R> LiveData<T>.compose(compose: Compose<T, R>): LiveData<R> = compose(this@compose)

fun <T, R> LiveData<T>.wrap(wrap: (value: T, subscriber: Subscriber<R>) -> Unit): LiveData<R> =
        mediate { addSource, setValue ->
            addSource { value ->
                wrap(value, setValue)
            }
        }

fun <T, R> LiveData<T>.lift(lift: (subscriber: Subscriber<R>) -> Subscriber<T>): LiveData<R> =
        mediate { addSource, setValue ->
            lift(setValue).let(addSource)
        }

fun <T> LiveData<T>.filter(predicate: (T) -> Boolean): LiveData<T> =
        wrap { value, next ->
            value.takeIf(predicate)?.let(next)
        }

@Suppress("UNCHECKED_CAST")
inline fun <reified R> LiveData<*>.cast(): LiveData<R> = map { it as R }

inline fun <reified R> LiveData<*>.castIf(): LiveData<R> = filter { it is R }.cast()

fun <T> LiveData<T>.first(): LiveData<T> = take(1)
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

fun <T, R> LiveData<T>.reduce(seed: R, accumulator: Accumulator<T, R>): LiveData<R> =
        reduce<T, R>({ seed }, accumulator)

@Suppress("UNCHECKED_CAST")
fun <T, R> LiveData<T>.reduce(getSeed: () -> R, accumulator: Accumulator<T, R>): LiveData<R> =
        lift { next ->
            var seed: Any? = Uninitialized

            { value ->
                if (seed is Uninitialized) {
                    seed = getSeed()
                    next(seed as R)
                }

                seed = accumulator(seed as R, value)
                next(seed as R)
            }
        }


fun <T> LiveData<T>.startWith(value: T): LiveData<T> = startWith<T> { value }

fun <T> LiveData<T>.startWith(getValue: () -> T): LiveData<T> =
        lift { next ->
            getValue().let(next);
            { value ->
                next(value)
            }
        }