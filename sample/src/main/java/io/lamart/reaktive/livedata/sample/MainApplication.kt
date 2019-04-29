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

import android.app.Application
import io.lamart.livedata.utils.Subscribe
import io.lamart.livedata.utils.mutableLiveDataOf
import io.lamart.reaktive.livedata.sample.utils.Order
import io.lamart.reaktive.livedata.sample.utils.State
import io.lamart.reaktive.livedata.sample.utils.ViewModelFactory

class MainApplication : Application() {

    val data by lazy { mutableLiveDataOf(State()) }
    val factory: ViewModelFactory by lazy {
        ViewModelFactory(this) {
            viewModel { (order: Subscribe<Order>) -> MainViewModel(data, order) }
        }
    }

}
