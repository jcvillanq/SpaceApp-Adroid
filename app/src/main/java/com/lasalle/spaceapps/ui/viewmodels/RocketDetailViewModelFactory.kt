package com.lasalle.spaceapps.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RocketDetailViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RocketDetailViewModel::class.java)) {
            return RocketDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}