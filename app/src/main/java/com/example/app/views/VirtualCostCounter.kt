package com.example.app.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.app.VirtualCostCounterViewModel

@Composable
fun VirtualCostCounter(
    smallLabelText: String,
    buttonText: String,
    modifier: Modifier,
    viewModel: VirtualCostCounterViewModel?
) {
    var totalCost = "0"
    viewModel?.let {
        totalCost = it.totalCost?.observeAsState()?.value.toString()
    }
    VirtualCostCounterLayout(
        {
            Text(
                text = totalCost,
                fontSize = 150.sp,
            )
        },
        {
            Text(
                text = smallLabelText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        {
            Button(
                onClick = { viewModel?.onShowBannerPressed() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .padding(vertical = 60.dp)
                    .height(60.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(
                    text = buttonText,
                    color = Color.White,
                    fontSize = 17.sp,
                )
            }
        },
        modifier = modifier
    )
}

@Composable
private fun VirtualCostCounterLayout(
    counter: @Composable () -> Unit,
    info: @Composable () -> Unit = {},
    button: @Composable () -> Unit,
    modifier: Modifier = Modifier,

    ) {
    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .layoutId("counter")
            ) {
                counter()
            }
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .layoutId("info")

            ) {
                info()
            }
        }
        Box(
            modifier = Modifier
                .layoutId("button")
                .then(modifier),
        ) {
            button()
        }
    }
}

private val constraintSet = ConstraintSet {
    val counter = createRefFor("counter")
    val info = createRefFor("info")
    val button = createRefFor("button")

    constrain(counter) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        bottom.linkTo(info.bottom)
    }
    constrain(info) {
        top.linkTo(counter.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        bottom.linkTo(button.bottom)
    }
    constrain(button) {
        bottom.linkTo(parent.bottom)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VirtualCostCounter(
        smallLabelText = "Consent Score",
        buttonText = "Show Consent Banner",
        modifier = Modifier,
        viewModel = null
    )
}