package com.lasalle.spaceapps.ui.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lasalle.spaceapps.data.local.SpaceAppsDatabase
import com.lasalle.spaceapps.data.model.Rocket
import com.lasalle.spaceapps.data.remote.RetrofitInstance
import com.lasalle.spaceapps.data.repository.RocketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.app.Application

class RocketDetailViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = RocketRepository(
        RetrofitInstance.api,
        SpaceAppsDatabase.getDatabase(application).rocketDao()
    )

    private val _rocket = MutableStateFlow<Rocket?>(null)
    val rocket: StateFlow<Rocket?> = _rocket

    fun loadRocket(rocketId: String) {
        viewModelScope.launch {
            _rocket.value = repository.getRocketById(rocketId)
        }
    }
}