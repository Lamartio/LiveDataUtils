package io.lamart.livedata.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import org.junit.Rule
import org.junit.Test

class TransformerTests {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @Test
    fun filter() {
        liveData(1)
                .filter { it == 1 }
                .test()
                .assertValue(1)

        liveData(1)
                .filter { it == 0 }
                .test()
                .assertNoValue()
    }

    @Test
    fun wrap() {
        liveData(1)
                .wrap<Int, String> { value, next ->
                    value.toString()
                            .also(next)
                            .also(next)
                }
                .test()
                .assertValueHistory("1", "1")
    }

    @Test
    fun cast() {
        liveData("1")
                .cast<CharSequence>()
                .test()
                .assertHasValue()

        try {
            liveData("")
                    .cast<Int>()
                    .test()
                    .assertNoValue()
        } catch (e: ClassCastException) {
        }
    }

    @Test
    fun castIf() {
        liveData("")
                .castIf<CharSequence>()
                .test()
                .assertHasValue()

        liveData("")
                .castIf<Int>()
                .test()
                .assertNoValue()
    }

    @Test
    fun lift() {
        liveData(1)
                .lift<Int, String> { next ->
                    { value ->
                        value.toString()
                                .also(next)
                                .also(next)
                    }
                }
                .test()
                .assertValueHistory("1", "1")
    }


    @Test
    fun prepend() {
        liveData(1)
                .prepend(2)
                .test()
                .assertValueHistory(2, 1)
    }

    @Test
    fun take() {
        val observable = liveData<Int>()
        val observer = observable.take(2).test()

        observable.setValues(1, 2, 3)
        observer.assertValueHistory(1, 2)
    }

    @Test
    fun first() {
        val observable = liveData<Int>()
        val observer = observable.first().test()

        observable.setValues(1, 2, 3)
        observer.assertValue(1)
    }

    @Test
    fun skip() {
        val observable = liveData<Int>()
        val observer = observable.skip(2).test()

        observable.setValues(1, 2, 3)
        observer.assertValue(3)
    }

    @Test
    fun reduce() {
        val observable = liveData<Int>()
        val observer = observable
                .reduce { l, r -> l + r }
                .test()

        observable.setValues(1, 2, 3)
        observer.assertValueHistory(1, 3, 6)
    }

    @Test
    fun reduceWithSeed() {
        val observable = liveData<Int>()
        val observer = observable
                .reduce(3.0) { l: Double, r: Int -> l + r }
                .test()

        observable.setValues(1, 2)
        observer.assertValueHistory(3.0, 4.0, 6.0)
    }

    @Test
    fun startWith() {
        val observable = liveData<Int>()
        val observer = observable
                .startWith(1)
                .test()

        observable.setValues(2, 3)
        observer.assertValueHistory(1, 2, 3)
    }

}
