package com.example.virtualcost.views

import android.content.Context
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text

class VirtualCostCounter(context: Context?) : View(context) {

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    VirtualCostCounter(RequireContext())
//}