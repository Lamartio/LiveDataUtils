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
        liveData(1.0)
            .combineLatest(subscribe(2)) { l, r -> l.plus(r).toString() }
            .test()
            .assertValue("3.0")
    }

    @Test
    fun merge() {
        liveData(1)
            .merge(subscribe(2))
            .test()
            .assertValueHistory(1, 2)
    }

    @Test
    fun pair() {
        val observable = liveData<Int>()
        val observer = observable.pair().test()

        observable.setValues(1, 2)
        observer.assertValue(1 to 2)
    }

    @Test
    fun window() {
        val observable = liveData<Int>()
        val observer = observable.window(2).test()

        observable.setValues(1, 2, 3)
        observer.assertValue(listOf(2, 3))
        partialWindow()
    }

    @Test
    fun partialWindow() {
        val observable = liveData<Int>()
        val observer = observable.window(2, true).test()

        observable.value = 1
        observer.assertValue(listOf(1))
    }


    @Test
    fun buffer() {
        val observable = liveData<Int>()
        val observer = observable.buffer(2).test()

        observable.setValues(1, 2, 3)
        observer.assertValue(listOf(1, 2))
    }

}
