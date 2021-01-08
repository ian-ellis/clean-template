package com.github.ianellis.clean.catfacts.summaries

import androidx.lifecycle.LifecycleObserver
import com.github.ianellis.clean.presentation.catfacts.summary.CatFactSummaryNavigator
import com.github.ianellis.clean.presentation.utils.navigator.ActivityLifecycleOwnerManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ianellis.github.clean.domain.common.catfacts.summary.CatFactSummary

class AppCatFactSummariesNavigator : CatFactSummaryNavigator, ActivityLifecycleOwnerManager() {

    override val lifecycleObserver: LifecycleObserver = this

    override fun summarySelected(summary: CatFactSummary) {
        owner?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage("not implemented yet")
                .create()
                .show()
        }
    }
}
