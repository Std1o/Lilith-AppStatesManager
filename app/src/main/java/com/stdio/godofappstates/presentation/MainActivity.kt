package com.stdio.godofappstates.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stdio.godofappstates.R
import stdio.godofappstates.annotations.AllStatesReadyToUse

@AllStatesReadyToUse
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}