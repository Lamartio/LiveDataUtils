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
import androidx.lifecycle.map
import com.jraska.livedata.test
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class MiscTests {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @Test
    fun afterEach() {
        var result = 0

        liveData(1)
                .afterEach { result = it }
                .test()
                .assertValue(1)

        assertEquals(1, result)
    }

    @Test
    fun beforeEach() {
        var result = 0

        liveData(1)
                .beforeEach { result = it }
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
