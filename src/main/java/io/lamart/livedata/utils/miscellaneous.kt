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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap


fun <T> LiveData<T>.on(subscriber: Subscriber<T>): LiveData<T> =
    wrap { value, next ->
        subscriber(value)
        next(value)
    }

fun <T> LiveData<T>.post(): LiveData<T> =
    switchMap {
        liveData<T>().apply {
            postValue(it)
        }
    }

fun <T, R> LiveData<T>.post(compose: Compose<T, R>): LiveData<R> =
    compose(compose).post()


fun <T, R> LiveData<T>.mediate(block: (addSource: Subscribe<T>, setValue: Subscriber<R>) -> Unit): LiveData<R> =
    mediator {
        block(
            { addSource(this@mediate, it) },
            ::setValue
        )
    }

fun <T> MutableLiveData<T>.setValues(vararg values: T) = setValues(values.toList())

fun <T> MutableLiveData<T>.setValues(iterable: Iterable<T>) = setValues(iterable.asSequence())

fun <T> MutableLiveData<T>.setValues(sequence: Sequence<T>): MutableLiveData<T> = setValues(sequence::forEach)

fun <T> MutableLiveData<T>.setValues(subscribe: Subscribe<T>): MutableLiveData<T> =
    apply { subscribe(::setValue) }

fun <T> Observer<T>.toSubscriber(): Subscriber<T> = this@toSubscriber::onChanged

fun <T> Observe<T>.toSubscribe(): Subscribe<T> = { subscriber -> invoke(Observer(subscriber)) }

fun <T> Subscribe<T>.toLiveData(): LiveData<T> = liveData(this@toLiveData)
