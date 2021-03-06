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

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * It returns a function that represents observing this LiveData. Since it already has the LifecycleOwner, it doesn't need it anymore as an argument.
 */

fun <T> LiveData<T>.observe(owner: LifecycleOwner): Observe<T> =
        { observer -> this@observe.observe(owner, observer) }

/**
 * Allows observing the LiveData through a function instead of the `Observer` functional interface.
 */

fun <T> LiveData<T>.subscribe(owner: LifecycleOwner, subscriber: (T) -> Unit) =
        observe(owner, Observer(subscriber))

/**
 * It returns a function that represents subscribing to this LiveData. Since it already has the LifecycleOwner, it doesn't need it anymore as an argument.
 */

fun <T> LiveData<T>.subscribe(owner: LifecycleOwner): Subscribe<T> =
        { subscriber -> subscribe(owner, subscriber) }