package io.lamart.livedata.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(owner: LifecycleOwner): Observe<T> =
        { observer -> this@observe.observe(owner, observer) }

fun <T> LiveData<T>.subscribe(owner: LifecycleOwner, subscriber: (T) -> Unit) =
        observe(owner, Observer(subscriber))

fun <T> LiveData<T>.subscribe(owner: LifecycleOwner): Subscribe<T> =
        { subscriber -> observe(owner, Observer(subscriber)) }