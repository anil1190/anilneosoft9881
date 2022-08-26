package com.example.myapplication.ui.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.dto.RecipesItem
import com.example.myapplication.databinding.RecipeItemBinding
import com.example.myapplication.ui.component.MainViewModel

class RecipesAdapter(private val recipesListViewModel: MainViewModel, private val recipes: List<RecipesItem>) : RecyclerView.Adapter<RecipeViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
       val itemBinding = RecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int {
        return recipes.size
    }


}