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
import androidx.lifecycle.map
import com.jraska.livedata.test
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class MiscTests {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @Test
    fun on() {
        var result = 0

        liveData(1)
            .on { result = it }
            .test()
            .assertValue(1)

        assertEquals(1, result)
    }

    @Test
    fun post() {
        liveData(1)
            .post()
            .test()
            .assertValue(1)
    }

    @Test
    fun postWithCompose() {
        liveData(1)
            .post { it.map { it + 1 } }
            .test()
            .assertValue(2)
    }

    @Test
    fun setValues() {
        val observable = liveData<Int>()
        val observer = observable.test()

        observable.setValues(1, 2)
        observable.setValues(listOf(3, 4))
        observable.setValues(sequenceOf(5, 6))
        observable.setValues { subscriber: Subscriber<Int> ->
            subscriber(7)
            subscriber(8)
        }

        observer.assertValueHistory(1, 2, 3, 4, 5, 6, 7, 8)
    }

}
