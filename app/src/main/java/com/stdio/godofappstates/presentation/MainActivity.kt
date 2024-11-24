package com.stdio.godofappstates.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stdio.godofappstates.R
import dagger.hilt.android.AndroidEntryPoint
import stdio.godofappstates.annotations.AllStatesReadyToUse

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}