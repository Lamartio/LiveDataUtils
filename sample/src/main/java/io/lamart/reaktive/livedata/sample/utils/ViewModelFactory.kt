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

package io.lamart.reaktive.livedata.sample.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
        private val application: Application,
        block: Builder.() -> Unit = {}
) : ViewModelProvider.AndroidViewModelFactory(application) {

    private val viewModels = Builder()
            .apply(block)
            .viewModels
            .toMap()
    private var params = Params()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            try {
                viewModels[modelClass]
                        ?.invoke(params) as T
                        ?: super.create(modelClass)
            } finally {
                params = Params()
            }

    fun withParams(vararg params: Any): ViewModelFactory =
            also {
                it.params = params.toMutableList().apply { add(application) }.let(::Params)
            }

    inner class Builder {

        val viewModels = mutableMapOf<Class<*>, (Params) -> ViewModel>()

        inline fun <reified T : ViewModel> viewModel(crossinline block: (params: Params) -> T) =
                viewModels.set(T::class.java, { block(it) })

    }

    @Suppress("UNCHECKED_CAST")
    data class Params(private val params: List<Any> = emptyList()) {

        operator fun <T> component1() = get<T>(0)
        operator fun <T> component2() = get<T>(1)
        operator fun <T> component3() = get<T>(2)
        operator fun <T> component4() = get<T>(3)
        operator fun <T> component5() = get<T>(4)
        operator fun <T> component6() = get<T>(5)

        operator fun <T> get(index: Int) = params[index] as T

    }

}