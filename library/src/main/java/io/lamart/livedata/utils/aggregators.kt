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

fun <L, R, T> LiveData<L>.combineLatest(with: Subscribe<R>, combine: (left: L, right: R) -> T): LiveData<T> =
        combineLatest(liveData(with), combine)

@Suppress("UNCHECKED_CAST")
fun <L, R, T> LiveData<L>.combineLatest(with: LiveData<R>, combine: (left: L, right: R) -> T): LiveData<T> =
        mediator {
            var left: Any? = Uninitialized
            var right: Any? = Uninitialized

            fun emit() {
                if (left !is Uninitialized && right !is Uninitialized)
                    combine(left as L, right as R).let(::setValue)
            }

            addSource(this@combineLatest) { l ->
                left = l
                emit()
            }

            addSource(with) { r ->
                right = r
                emit()
            }
        }

fun <T> LiveData<T>.merge(with: Subscribe<T>): LiveData<T> = with.toLiveData().let(::merge)

fun <T> LiveData<T>.merge(with: LiveData<T>): LiveData<T> =
        mediator {
            addSource(this@merge, ::setValue)
            addSource(with, ::setValue)
        }

@Suppress("UNCHECKED_CAST")
fun <T> LiveData<T>.pair(): LiveData<Pair<T, T>> =
        lift { next ->
            var previous: Any? = Uninitialized

            { value ->
                if (previous != Uninitialized)
                    next(previous as T to value)

                previous = value
            }
        }

fun <T> LiveData<T>.window(size: Int, allowPartial: Boolean = false): LiveData<List<T>> =
        lift { next ->
            val window = mutableListOf<T>();

            { value ->
                window.add(value)

                if (window.size > size)
                    window.subList(0, window.size - size).clear()

                if (allowPartial || window.size == size)
                    window.toList().let(next)
            }
        }

fun <T> LiveData<T>.buffer(size: Int): LiveData<List<T>> =
        lift { next ->
            val buffer = mutableListOf<T>();

            { value ->
                buffer.add(value)

                if (buffer.size == size) {
                    val result = buffer.toList()

                    buffer.clear()
                    next(result)
                }
            }
        }