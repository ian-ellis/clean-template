package com.ianellis.github.clean.apppresentation.catfacts.summaries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.ianellis.clean.presentation.utils.recyclerview.diffutil.EqualityDiffUtil
import com.ianellis.github.clean.apppresentation.R
import com.ianellis.github.clean.apppresentation.databinding.RecyclerItemCatFactSummaryBinding
import com.ianellis.github.clean.domain.common.catfacts.summary.CatFactSummary

internal class CatFactSummariesAdapter : RecyclerView.Adapter<CatFactSummariesAdapter.ViewHolder>() {

    var summaries: List<CatFactSummary> = emptyList()
        set(value) {
            val diff = DiffUtil.calculateDiff(EqualityDiffUtil(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    val selected: ((CatFactSummary) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.recycler_item_cat_fact_summary,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = summaries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(summaries[position])
    }

    internal inner class ViewHolder(
        private val binding: RecyclerItemCatFactSummaryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(summary: CatFactSummary) {
            binding.summary = summary.text
            binding.onClick = View.OnClickListener {
                selected?.invoke(summary)
            }
        }
    }
}
