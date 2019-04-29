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
import com.jraska.livedata.TestLifecycle
import com.jraska.livedata.TestObserver
import org.junit.Rule
import org.junit.Test

class ObserverTests {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    private val owner = TestLifecycle.resumed()

    @Test
    fun observe() {
        val observer = TestObserver.create<Int>()

        liveData(1)
                .observe(owner)
                .invoke(observer)

        observer.assertHasValue()
    }

    @Test
    fun subscribeMonad() {
        val observer = TestObserver.create<Int>()

        liveData(1)
                .subscribe(owner)
                .invoke(observer::onChanged)

        observer.assertHasValue()
    }

    @Test
    fun subscribeDyad() {
        val observer = TestObserver.create<Int>()

        liveData(1)
                .subscribe(owner, observer::onChanged)

        observer.assertHasValue()
    }

}