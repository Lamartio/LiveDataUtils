package io.lamart.livedata.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

typealias Subscriber<T> = (value: T) -> Unit
typealias Compose<T, R> = (data: LiveData<T>) -> LiveData<R>
typealias Accumulator<T, R> = (seed: R, value: T) -> R
typealias Observe<T> = (observer: Observer<T>) -> Unit
typealias Subscribe<T> = (subscriber: Subscriber<T>) -> Unit

internal object Uninitialized