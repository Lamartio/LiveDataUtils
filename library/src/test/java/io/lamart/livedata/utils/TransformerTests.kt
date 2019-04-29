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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import org.junit.Rule
import org.junit.Test

class TransformerTests {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @Test
    fun filter() {
        mutableLiveDataOf(1)
                .filter { it == 1 }
                .test()
                .assertValue(1)

        mutableLiveDataOf(1)
                .filter { it == 0 }
                .test()
                .assertNoValue()
    }

    @Test
    fun wrap() {
        mutableLiveDataOf(1)
                .wrap<Int, String> { value, next ->
                    value.toString()
                            .also(next)
                            .also(next)
                }
                .test()
                .assertValueHistory("1", "1")
    }

    @Test
    fun cast() {
        mutableLiveDataOf("1")
                .cast<CharSequence>()
                .test()
                .assertHasValue()

        try {
            mutableLiveDataOf("")
                    .cast<Int>()
                    .test()
                    .assertNoValue()
        } catch (e: ClassCastException) {
        }
    }

    @Test
    fun castIf() {
        mutableLiveDataOf("")
                .castIf<CharSequence>()
                .test()
                .assertHasValue()

        mutableLiveDataOf("")
                .castIf<Int>()
                .test()
                .assertNoValue()
    }

    @Test
    fun lift() {
        mutableLiveDataOf(1)
                .lift<Int, String> { next ->
                    { value ->
                        value.toString()
                                .also(next)
                                .also(next)
                    }
                }
                .test()
                .assertValueHistory("1", "1")
    }


    @Test
    fun prepend() {
        mutableLiveDataOf(1)
                .prepend(2)
                .test()
                .assertValueHistory(2, 1)
    }

    @Test
    fun take() {
        val observable = mutableLiveDataOf<Int>()
        val observer = observable.take(2).test()

        observable.setValues(1, 2, 3)
        observer.assertValueHistory(1, 2)
    }

    @Test
    fun first() {
        val observable = mutableLiveDataOf<Int>()
        val observer = observable.first().test()

        observable.setValues(1, 2, 3)
        observer.assertValue(1)
    }

    @Test
    fun skip() {
        val observable = mutableLiveDataOf<Int>()
        val observer = observable.skip(2).test()

        observable.setValues(1, 2, 3)
        observer.assertValue(3)
    }

    @Test
    fun reduce() {
        val observable = mutableLiveDataOf<Int>()
        val observer = observable
                .reduce { l, r -> l + r }
                .test()

        observable.setValues(1, 2, 3)
        observer.assertValueHistory(1, 3, 6)
    }

    @Test
    fun reduceWithSeed() {
        val observable = mutableLiveDataOf<Int>()
        val observer = observable
                .reduce(3.0) { l: Double, r: Int -> l + r }
                .test()

        observable.setValues(1, 2)
        observer.assertValueHistory(3.0, 4.0, 6.0)
    }

    @Test
    fun startWith() {
        val observable = mutableLiveDataOf<Int>()
        val observer = observable
                .startWith(1)
                .test()

        observable.setValues(2, 3)
        observer.assertValueHistory(1, 2, 3)
    }

}
