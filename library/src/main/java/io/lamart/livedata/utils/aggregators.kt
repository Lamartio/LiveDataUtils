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

/**
 * Takes the latest of the LiveData and the first argument, combines them with the second argument and then emits the result.
 */

fun <L, R, T> LiveData<L>.combine(with: Subscribe<R>, combine: (left: L, right: R) -> T): LiveData<T> =
        combine(liveDataOf(with), combine)


/**
 * Takes the latest of each LiveData, combines them with the second argument and then emits the result.
 */

@Suppress("UNCHECKED_CAST")
fun <L, R, T> LiveData<L>.combine(with: LiveData<R>, combine: (left: L, right: R) -> T): LiveData<T> =
        mediatorLiveDataOf {
            var left: Any? = Uninitialized
            var right: Any? = Uninitialized

            fun emit() {
                if (left !is Uninitialized && right !is Uninitialized)
                    combine(left as L, right as R).let(::setValue)
            }

            addSource(this@combine) { l ->
                left = l
                emit()
            }

            addSource(with) { r ->
                right = r
                emit()
            }
        }

/**
 * Merges the LiveData with the given `Subscribe`.
 */

fun <T> LiveData<T>.merge(with: Subscribe<T>): LiveData<T> = with.toLiveData().let(::merge)

/**
 * Merges to LiveData into one.
 */

fun <T> LiveData<T>.merge(with: LiveData<T>): LiveData<T> =
        mediatorLiveDataOf {
            addSource(this@merge, ::setValue)
            addSource(with, ::setValue)
        }

/**
 * Whenever there are two emissions, the get bundled into a pair. Handy for before/after comparison
 */

fun <T> LiveData<T>.pair(seed: T): LiveData<Pair<T, T>> = pairUnsafe(seed)

/**
 * Whenever there are two emissions, the get bundled into a pair. Handy for before/after comparison
 */

fun <T> LiveData<T>.pair(): LiveData<Pair<T, T>> = pairUnsafe(Uninitialized)

@Suppress("UNCHECKED_CAST")
internal fun <T> LiveData<T>.pairUnsafe(seed: Any?): LiveData<Pair<T, T>> =
        lift { next ->
            var previous: Any? = seed

            { value ->
                if (previous != Uninitialized)
                    next(previous as T to value)

                previous = value
            }
        }

/**
 * Whenever there are `size` or more emissions the latest `size` will be bundled into a list and emitted.
 *
 * With `allowPartial` the initial lists can ve smaller than `size`.
 */

fun <T> LiveData<T>.window(size: Int, allowPartial: Boolean = false): LiveData<List<T>> {
    assert(size > 0)
    return lift { next ->
        val window = mutableListOf<T>();

        { value ->
            window.add(value)

            if (window.size > size)
                window.subList(0, window.size - size).clear()

            if (allowPartial || window.size >= size)
                window.toList().let(next)
        }
    }
}

/**
 * When the has a count of `size`, the values get bundled into a list and emitted. After emission the buffer gets cleared.
 */

fun <T> LiveData<T>.buffer(size: Int): LiveData<List<T>> {
    assert(size > 0)
    return lift { next ->
        val buffer = mutableListOf<T>();

        { value ->
            buffer.add(value)

            if (buffer.size >= size) {
                val result = buffer.toList()

                buffer.clear()
                next(result)
            }
        }
    }
}