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

class CreatorTests {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @Test
    fun toMutableLiveData() {
        liveData<Int> { next -> next(1) }
                .toMutableLiveData()
                .test()
                .assertValue(1)
    }

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