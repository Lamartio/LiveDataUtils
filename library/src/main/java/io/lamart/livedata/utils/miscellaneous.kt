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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap


fun <T> LiveData<T>.beforeEach(subscriber: Subscriber<T>): LiveData<T> =
        wrap { value, next ->
            subscriber(value)
            next(value)
        }

fun <T> LiveData<T>.afterEach(subscriber: Subscriber<T>): LiveData<T> =
        wrap { value, next ->
            next(value)
            subscriber(value)
        }

fun <T> LiveData<T>.post(): LiveData<T> =
        switchMap {
            mutableLiveDataOf<T>().apply {
                postValue(it)
            }
        }

fun <T, R> LiveData<T>.post(compose: Compose<T, R>): LiveData<R> =
        compose(compose).post()


fun <T, R> LiveData<T>.mediate(block: (addSource: Subscribe<T>, setValue: Subscriber<R>) -> Unit): LiveData<R> =
        mediatorLiveDataOf {
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

fun <T> Subscribe<T>.toLiveData(): LiveData<T> = liveDataOf(this@toLiveData)
