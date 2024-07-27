package com.example.app.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VirtualCostCounterViewModel: ViewModel() {

    private val _totalCost =
        MutableLiveData<Int>(0)
    val totalCost: LiveData<Int> = _totalCost

    fun onShowBannerPressed() {
        TODO("Not yet implemented")
    }

}