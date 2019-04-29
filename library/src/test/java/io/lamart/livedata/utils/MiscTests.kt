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
