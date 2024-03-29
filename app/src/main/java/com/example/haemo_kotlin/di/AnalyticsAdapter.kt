package com.example.haemo_kotlin.di

import android.content.Context
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class AnalyticsAdapter @Inject constructor(
    @ActivityContext private val context: Context,
//    private val service: AnalyticsService
) { }