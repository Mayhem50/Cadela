package com.whitecards.cadela.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whitecards.cadela.R
import com.whitecards.cadela.data.model.Exercise
import com.whitecards.cadela.data.model.Program
import com.whitecards.cadela.databinding.ListItemExerciseBinding
import com.whitecards.cadela.viewModel.ExerciseListItemViewModel

class ExerciceAdapter(private val program: Program) :
    ListAdapter<Exercise, ExerciceAdapter.ViewHolder>(ExerciseItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_exercise,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let {
            with(holder) {
                bind(program)
            }
        }
    }

    class ViewHolder(private val binding: ListItemExerciseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(program: Program) {
            with(binding) {
                vm = ExerciseListItemViewModel(program, adapterPosition)
            }
        }
    }
}

private class ExerciseItemDiffCallback : DiffUtil.ItemCallback<Exercise>() {
    override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem == newItem
    }
}