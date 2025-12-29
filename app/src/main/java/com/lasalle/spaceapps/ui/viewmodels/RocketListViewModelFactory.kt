package com.lasalle.spaceapps.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RocketListViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RocketListViewModel::class.java)) {
            return RocketListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}