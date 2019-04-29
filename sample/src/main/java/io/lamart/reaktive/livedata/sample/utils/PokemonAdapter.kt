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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.lamart.reaktive.livedata.sample.R
import io.lamart.reaktive.livedata.sample.databinding.PokemonItemBinding

class PokemonAdapter(context: Context) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private val pokemon = mutableListOf<String>()

    fun update(pokemon: List<String>) {
        this.pokemon.clear()
        this.pokemon.addAll(pokemon)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            inflater
                    .inflate(R.layout.pokemon_item, parent, false)
                    .let(PokemonAdapter::ViewHolder)

    override fun getItemCount(): Int = pokemon.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            pokemon[position].let(holder::bind)


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = DataBindingUtil.bind<PokemonItemBinding>(itemView)!!

        fun bind(pokemon: String) = binding.setName(pokemon)

    }
}