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