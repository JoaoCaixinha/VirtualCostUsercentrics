package com.example.app.views

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.app.VirtualCostCounterViewModel
import com.example.app.VirtualCostCounterViewModel.VirtualCostCounterEvent.ShowFirstLayer
import com.example.app.VirtualCostCounterViewModel.VirtualCostCounterEvent.ShowSecondLayer
import com.example.app.VirtualCostCounterViewModel.VirtualCostCounterEvent.SetUserAsFirstTime
import com.example.app.VirtualCostCounterViewModel.VirtualCostCounterEvent.AsUserAsFirstTime
import com.example.app.views.theme.VirtualCostTheme
import com.example.virtualcost.R
import com.usercentrics.sdk.Usercentrics
import com.usercentrics.sdk.UsercentricsBanner
import com.usercentrics.sdk.UsercentricsOptions

const val ACCEPT_ALL = "ACCEPT_ALL"

class MainActivity : ComponentActivity() {
    private val viewModel: VirtualCostCounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val options = UsercentricsOptions(settingsId = getString(R.string.UserCentricsSettingsID))
        Usercentrics.initialize(this, options)

        viewModel.virtualCostCounterEvents.observe(this) { data ->
            when (data) {
                is ShowFirstLayer -> showFirstLayer()
                is ShowSecondLayer -> showSecondLayer()
                is SetUserAsFirstTime -> setUserAsFirstTime(data.given)
                is AsUserAsFirstTime -> asUserAsFirstTime()
            }
        }

        enableEdgeToEdge()
        setContent {
            VirtualCostTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VirtualCostCounter(
                        smallLabelText = stringResource(R.string.consent_score),
                        buttonText = stringResource(R.string.show_consent_banner),
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    private fun showFirstLayer() {
        val banner = UsercentricsBanner(this)
        banner.showFirstLayer() { userResponse ->
            viewModel.setUserAsFirstTime(userResponse?.userInteraction?.name == ACCEPT_ALL)
            viewModel.applyConsent(userResponse?.consents)
        }
    }

    private fun showSecondLayer() {
        val banner = UsercentricsBanner(this)
        banner.showSecondLayer() { userResponse ->
            viewModel.applyConsent(userResponse?.consents)
        }
    }

    private fun setUserAsFirstTime(given: Boolean) {
        val sharedPref = getSharedPreferences(getString(R.string.virtualcoststorage), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(getString(R.string.userasgivenforfirsttime), given)
        editor.apply()
    }

   private fun asUserAsFirstTime() {
        val sharedPref = getSharedPreferences(getString(R.string.virtualcoststorage), Context.MODE_PRIVATE)
        val result = sharedPref.getBoolean(getString(R.string.userasgivenforfirsttime), false)
        viewModel.setUserAsFirstTime(result)
    }

}
