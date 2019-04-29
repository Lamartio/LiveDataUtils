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

class CreatorTests {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @Test
    fun livedataWithoutInitialValue() {
        val observable = liveData<Int>()
        val observer = observable.test()

        observer.assertNoValue()
        observable.value = 1
        observer.assertValue(1)
        observable.value = 2
        observer.assertValueHistory(1, 2)
    }

    @Test
    fun livedataWithInitialValue() {
        val observable = liveData(1)
        val observer = observable.test()

        observer.assertValue(1)
        observable.value = 2
        observer.assertValueHistory(1, 2)
    }

    @Test
    fun livedataFromSubscribe() {
        liveData<Int> { subscriber ->
            subscriber.invoke(1)
        }.test().assertValue(1)
    }

    @Test
    fun mediate() {
        liveData(1)
            .mediate<Int, Int> { addSource, setValue ->
                addSource { value ->
                    setValue(value + value)
                }
            }
            .test()
            .assertValue(2)
    }

    @Test
    fun mediator() {
        mediator<Int> {
            addSource(liveData(1)) { value ->
                setValue(value + value)
            }
        }
            .test()
            .assertValue(2)
    }
}