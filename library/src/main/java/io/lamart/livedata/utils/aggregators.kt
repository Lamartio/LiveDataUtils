/*
 * Copyright 2019 Danny Lamarti
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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