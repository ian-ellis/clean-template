package com.github.ianellis.clean.presentation.utils.recyclerview.diffutil

import androidx.recyclerview.widget.DiffUtil

class EqualityDiffUtil<T : Any>(
    private val old: List<T>,
    private val new: List<T>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == new[newItemPosition]
    }

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = true
}
