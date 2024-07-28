package com.example.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.usercentrics.sdk.Usercentrics
import com.usercentrics.sdk.UsercentricsServiceConsent

class VirtualCostCounterViewModel: ViewModel() {
    private val _virtualCostCounterEvents = MutableLiveData<VirtualCostCounterEvent>()
    public val virtualCostCounterEvents: LiveData<VirtualCostCounterEvent> = _virtualCostCounterEvents

    private val _totalCost = MutableLiveData<Int>(0)
    val totalCost: LiveData<Int> = _totalCost.distinctUntilChanged()

    private val _isFirstTime= MutableLiveData<Boolean>(false)

    fun setUserAsFirstTime(given: Boolean){
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
        if(_isFirstTime.value == true) {
            _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.ShowFirstLayer)
        }else{
            _virtualCostCounterEvents.postValue(VirtualCostCounterEvent.ShowSecondLayer)
        }
    }

    fun applyConsent(consents: List<UsercentricsServiceConsent>?) {
            consents?.forEach { service ->
                when (service.templateId) {
                    "diWdt4yLB" -> { // Google Analytics for Firebase Template ID

                        // Google Firebase Consent Mode API
//                    val firebaseConsentStatus = if (service.status) ConsentStatus.GRANTED else ConsentStatus.DENIED
//                    Firebase.analytics.setConsent {
//                        analyticsStorage(firebaseConsentStatus)
//                        adStorage(firebaseConsentStatus)
//                    }
//                    initializeFirebase()

                    }
                    // Other Service Template ID
                    "x-XXXxXx" -> {
                        // Initialize or pass consent to framework with service.status
                    }

                    else -> {
                        // Log a warning if a service was not caught or do nothing
                    }
                }
            }
    }

    sealed class VirtualCostCounterEvent(val data: Any?) {
        data object ShowFirstLayer : VirtualCostCounterEvent(null)
        data object ShowSecondLayer : VirtualCostCounterEvent(null)
        data class SetUserAsFirstTime(val given: Boolean) : VirtualCostCounterEvent(given)
        data object AsUserAsFirstTime : VirtualCostCounterEvent(null)
    }
}

