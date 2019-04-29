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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.lamart.livedata.utils.mutableLiveDataOf
import io.lamart.livedata.utils.subscribe
import io.lamart.reaktive.livedata.sample.databinding.MainFragmentBinding
import io.lamart.reaktive.livedata.sample.utils.Order
import io.lamart.reaktive.livedata.sample.utils.PokemonAdapter
import io.lamart.reaktive.livedata.sample.utils.viewModel

class MainFragment : Fragment() {

    private val order = mutableLiveDataOf(Order.ASCENDING)
    private val viewModel by viewModel<MainViewModel>(order.subscribe(this))

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = DataBindingUtil
            .inflate<MainFragmentBinding>(inflater, R.layout.main_fragment, container, false)
            .apply(::init)
            .root

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.let { menu ->
            menu.add("Ascending").setOnMenuItemClickListener { order.value = Order.ASCENDING;true }
            menu.add("Descending").setOnMenuItemClickListener { order.value = Order.DESCENDING;true }
        }
    }

    private fun init(binding: MainFragmentBinding) = with(binding) {
        val adapter = PokemonAdapter(root.context)

        lifecycleOwner = this@MainFragment
        model = viewModel
        viewModel.pokemon.subscribe(this@MainFragment, adapter::update)

        pokemonView.layoutManager = LinearLayoutManager(root.context)
        pokemonView.adapter = adapter
        pokemonView.addItemDecoration(DividerItemDecoration(root.context, DividerItemDecoration.VERTICAL))

    }

    companion object {
        fun newInstance() = MainFragment()
    }

}


