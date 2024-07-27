package com.example.app.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.app.views.theme.VirtualCostTheme

class MainActivity : ComponentActivity() {
    val viewModel: VirtualCostCounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VirtualCostTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VirtualCostCounter(
                        "Consent Score",
                        "Show Consent Banner",
                        {},
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }



    }
}
