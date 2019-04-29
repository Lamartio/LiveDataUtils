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
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

fun <T> mediatorLiveDataOf(block: MediatorLiveData<T>.() -> Unit): MutableLiveData<T> =
        MediatorLiveData<T>().apply(block)

fun <T> mutableLiveDataOf() = MutableLiveData<T>()

fun <T> mutableLiveDataOf(value: T) = MutableLiveData<T>(value)

fun <T> liveDataOf(subscribe: Subscribe<T>): LiveData<T> =
        MutableLiveData<T>().apply {
            subscribe(::setValue)
        }

fun <T> LiveData<T>.toMutableLiveData(): MutableLiveData<T> =
        mediatorLiveDataOf { addSource(this@toMutableLiveData, ::setValue) }