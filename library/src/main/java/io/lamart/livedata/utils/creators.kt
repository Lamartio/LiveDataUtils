package io.lamart.livedata.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

fun <T> mediator(block: MediatorLiveData<T>.() -> Unit): MutableLiveData<T> =
        MediatorLiveData<T>().apply(block)

fun <T> liveData() = MutableLiveData<T>()

fun <T> liveData(value: T) = MutableLiveData<T>(value)

fun <T> liveData(subscribe: Subscribe<T>): LiveData<T> =
        MutableLiveData<T>().apply {
            subscribe(::setValue)
        }