package com.ianellis.github.clean.apppresentation.catfacts.summaries

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ianellis.github.clean.domain.common.catfacts.summary.CatFactSummary

class CatFactSummariesRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private val summariesAdapter = CatFactSummariesAdapter()

    var summaries: List<CatFactSummary>
        get() = summariesAdapter.summaries
        set(value) {
            summariesAdapter.summaries = value
        }

    init {
        adapter = summariesAdapter
        layoutManager = LinearLayoutManager(context)
    }
}
