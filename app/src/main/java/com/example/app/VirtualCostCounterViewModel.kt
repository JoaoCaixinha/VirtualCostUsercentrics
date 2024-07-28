package com.example.app

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.usercentrics.sdk.Usercentrics
import com.usercentrics.sdk.UsercentricsServiceConsent
import com.usercentrics.sdk.v2.settings.data.UsercentricsService

const val VirtualCostCounter = "VirtualCostCounter"

class VirtualCostCounterViewModel : ViewModel() {
    private val _virtualCostCounterEvents = MutableLiveData<VirtualCostCounterEvent>()
    public val virtualCostCounterEvents: LiveData<VirtualCostCounterEvent> =
        _virtualCostCounterEvents

    private val _totalCost = MutableLiveData<Int>(0)
    val totalCost: LiveData<Int> = _totalCost.distinctUntilChanged()

    private val _isFirstTime = MutableLiveData<Boolean>(false)

    fun setUserAsFirstTime(given: Boolean) {
        _isFirstTime.value = given
        _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.AsUserAsFirstTime)
    }

    init {
        //get storage info if user as pass from first layer
        _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.AsUserAsFirstTime)

        Usercentrics.isReady({ status ->

            if (status.shouldCollectConsent) {
                // first time or when prompting updates to the consent services
                _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.SetUserAsFirstTime(false))
            } else {
                applyConsent(status.consents)
            }
        }, { error ->
            // Handle non-localized error
        })
    }

    fun onShowBannerPressed() {
        if (_isFirstTime.value == true) {
            _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.ShowFirstLayer)
        } else {
            _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.ShowSecondLayer)
        }
    }

    fun applyConsent(consents: List<UsercentricsServiceConsent>?) {
        _totalCost.value = 0
        val services = Usercentrics.instance.getCMPData().services
        consents?.forEach { consent ->
            when (consent.status) {
                true -> {
                    findService(consent.templateId, services)?.let { service ->
                        val cost = calculateCost(service).toInt()
                        _totalCost.value = _totalCost.value?.plus(cost)
                        Log.d(VirtualCostCounter, "${consent.dataProcessor} = $cost")
                    }
                }

                false -> {}// ignore not consented
            }
        }
        val total = _totalCost.value
        Log.d(VirtualCostCounter, "Total = $total")
    }

    private fun findService(
        consentTemplateId: String,
        services: List<UsercentricsService>
    ): UsercentricsService? {
        return services.find { it.templateId == consentTemplateId }
    }

    private fun calculateCost(service: UsercentricsService): Double {
        var cost = 0.0
        var extraFound = false
        service.dataCollectedList.forEach { data ->
            val dataCollected = DataCostTable.getEnumFromDataCollectedString(data)
            // if dataCollected is null, the string in dataCollectedList is not in the enum DataCostTable.
            // on the exercise there no specification how to handle the situation
            dataCollected?.let {
                cost += DataCostTable.getIncrementCost(dataCollected)
                val asExtraCost = DataCostTable.getExtraPercentageCost(dataCollected)
                if (!extraFound && asExtraCost > 10.0) {
                    extraFound = true
                    cost *= asExtraCost
                }
            }
        }

        cost = DataCostTable.getBonusPercentage(cost, service.dataCollectedList.count())

        return cost
    }

    sealed class VirtualCostCounterEvent(val data: Any?) {
        data object ShowFirstLayer : VirtualCostCounterEvent(null)
        data object ShowSecondLayer : VirtualCostCounterEvent(null)
        data class SetUserAsFirstTime(val given: Boolean) : VirtualCostCounterEvent(given)
        data object AsUserAsFirstTime : VirtualCostCounterEvent(null)
    }
}

