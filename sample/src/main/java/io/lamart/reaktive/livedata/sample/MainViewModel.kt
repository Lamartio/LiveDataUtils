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

package io.lamart.reaktive.livedata.sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import io.lamart.livedata.utils.Subscribe
import io.lamart.livedata.utils.withLatestFrom
import io.lamart.reaktive.livedata.sample.utils.Order
import io.lamart.reaktive.livedata.sample.utils.State


class MainViewModel(
        private val data: MutableLiveData<State>,
        order: Subscribe<Order>
) : ViewModel() {

    val count: LiveData<String> = data
            .map { it.count.toString() }
    val pokemon = data
            .map { it.pokemon }
            .withLatestFrom(order) { pokemon, order ->
                when (order) {
                    Order.ASCENDING -> pokemon.sorted()
                    Order.DESCENDING -> pokemon.sortedDescending()
                }
            }

    fun increment() =
            data.update { state ->
                state.copy(count = state.count + 1)
            }

    fun decrement() =
            data.update { state ->
                state.copy(count = state.count - 1)
            }

}

fun <T> MutableLiveData<T>.update(block: (state: T) -> T) {
    value?.run(block)?.let(::setValue)
}