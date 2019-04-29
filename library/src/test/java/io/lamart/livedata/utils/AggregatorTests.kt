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

class AggregatorTests {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    private fun <T> subscribe(value: T): Subscribe<T> = { subscriber: Subscriber<T> -> subscriber.invoke(value) }

    @Test
    fun combineLatest() {
        mutableLiveDataOf(1.0)
                .combineLatest(subscribe(2)) { l, r -> l.plus(r).toString() }
                .test()
                .assertValue("3.0")
    }

    @Test
    fun merge() {
        mutableLiveDataOf(1)
                .merge(subscribe(2))
                .test()
                .assertValueHistory(1, 2)
    }

    @Test
    fun pair() {
        val observable = mutableLiveDataOf<Int>()
        val observer = observable.pair().test()

        observable.setValues(1, 2)
        observer.assertValue(1 to 2)
    }

    @Test
    fun pairWithSeed() {
        val observable = mutableLiveDataOf<Int>()
        val observer = observable.pair(1).test()

        observable.setValues(2)
        observer.assertValue(1 to 2)
    }

    @Test
    fun window() {
        val observable = mutableLiveDataOf<Int>()
        val observer = observable.window(2).test()

        observable.setValues(1, 2, 3)
        observer.assertValue(listOf(2, 3))
        partialWindow()
    }

    @Test
    fun partialWindow() {
        val observable = mutableLiveDataOf<Int>()
        val observer = observable.window(2, true).test()

        observable.value = 1
        observer.assertValue(listOf(1))
    }


    @Test
    fun buffer() {
        val observable = mutableLiveDataOf<Int>()
        val observer = observable.buffer(2).test()

        observable.setValues(1, 2, 3)
        observer.assertValue(listOf(1, 2))
    }

}
