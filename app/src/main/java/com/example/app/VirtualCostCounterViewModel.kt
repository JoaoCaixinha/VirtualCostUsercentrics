package com.example.app

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.example.app.DataCostTable.Companion.calculateCost
import com.usercentrics.sdk.Usercentrics
import com.usercentrics.sdk.UsercentricsServiceConsent
import com.usercentrics.sdk.v2.settings.data.UsercentricsService

const val VirtualCostCounter = "VirtualCostCounter"

class VirtualCostCounterViewModel : ViewModel() {
    private val _virtualCostCounterEvents = MutableLiveData<VirtualCostCounterEvent>()
    val virtualCostCounterEvents: LiveData<VirtualCostCounterEvent> =
        _virtualCostCounterEvents

    private val _totalCost = MutableLiveData<Int>(0)
    val totalCost: LiveData<Int> = _totalCost.distinctUntilChanged()

    private val _isFirstTime = MutableLiveData<Boolean>(true)
    val isFirstTime: LiveData<Boolean> = _isFirstTime.distinctUntilChanged()

    init {
        //get storage info if user as pass from first layer
        _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.AsUserAsFirstTimeEvent)

        Usercentrics.isReady({ status ->
            if (status.shouldCollectConsent) {
                // first time or when prompting updates to the consent services
                _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.SetUserAsFirstTimeEvent(false))
            } else {
                applyConsent(status.consents)
            }
        }, { error ->
            // on error lets back to first layer
            _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.SetUserAsFirstTimeEvent(true))
            //reset usercentrics sdk
            Usercentrics.reset()
        })
    }

    /*
   * stores variable if user as accepted the banner first layer
   * */
    fun setUserAsFirstTime(given: Boolean) {
        _isFirstTime.value = given
    }

    /*
   * button action show consent banner
   * */
    fun onShowBannerPressed() {
        if (_isFirstTime.value == true) {
            _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.ShowFirstLayer)
        } else {
            _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.ShowSecondLayer)
        }
    }

    /*
   * handles consent accept and calculates cost according per service
   * */
    fun applyConsent(consents: List<UsercentricsServiceConsent>?) {
        _totalCost.value = 0
        val services = Usercentrics.instance.getCMPData().services
        consents?.forEach { consent ->
            when (consent.status) {
                true -> {
                    findService(consent.templateId, services)?.let { service ->
                        val cost = calculateCost(service.dataCollectedList).toInt()
                        _totalCost.value = _totalCost.value?.plus(cost)
                        Log.d(VirtualCostCounter, "${consent.dataProcessor} = $cost")
                    }
                }

                false -> {}// ignore not consent given
            }
        }
        val total = _totalCost.value
        Log.d(VirtualCostCounter, "Total = $total")
    }

    /*
    * find service with same id than consent
    * */
    private fun findService(
        consentTemplateId: String,
        services: List<UsercentricsService>
    ): UsercentricsService? {
        return services.find { it.templateId == consentTemplateId }
    }

    sealed class VirtualCostCounterEvent(val data: Any?) {
        data object ShowFirstLayer : VirtualCostCounterEvent(null)
        data object ShowSecondLayer : VirtualCostCounterEvent(null)
        data class SetUserAsFirstTimeEvent(val given: Boolean) : VirtualCostCounterEvent(given)
        data object AsUserAsFirstTimeEvent : VirtualCostCounterEvent(null)
    }
}

