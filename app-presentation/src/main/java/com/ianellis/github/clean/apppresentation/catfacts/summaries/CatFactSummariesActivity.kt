package com.ianellis.github.clean.apppresentation.catfacts.summaries

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.ianellis.clean.presentation.catfacts.summary.CatFactSummariesViewModel
import com.ianellis.github.clean.apppresentation.R
import com.ianellis.github.clean.apppresentation.databinding.ActivityCatFactSummariesBinding
import dagger.android.AndroidInjection
import javax.inject.Inject

class CatFactSummariesActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: CatFactSummariesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityCatFactSummariesBinding>(
            this,
            R.layout.activity_cat_fact_summaries
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        lifecycle.addObserver(viewModel.navigator.lifecycleObserver)
    }
}
