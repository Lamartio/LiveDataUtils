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