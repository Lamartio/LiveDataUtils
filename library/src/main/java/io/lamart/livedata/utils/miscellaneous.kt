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
