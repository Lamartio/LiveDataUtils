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