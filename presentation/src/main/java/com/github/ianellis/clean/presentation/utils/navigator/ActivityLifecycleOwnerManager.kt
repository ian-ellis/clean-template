package com.github.ianellis.clean.presentation.utils.navigator

import androidx.appcompat.app.AppCompatActivity

open class ActivityLifecycleOwnerManager : AbstractLifecycleOwnerManager<AppCompatActivity>(AppCompatActivity::class)
