package com.lasalle.spaceapps.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lasalle.spaceapps.data.local.SpaceAppsDatabase
import com.lasalle.spaceapps.data.model.Rocket
import com.lasalle.spaceapps.data.remote.RetrofitInstance
import com.lasalle.spaceapps.data.repository.RocketRepository
import com.lasalle.spaceapps.data.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RocketListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RocketRepository(
        RetrofitInstance.api,
        SpaceAppsDatabase.getDatabase(application).rocketDao()
    )

    private val _uiState = MutableStateFlow<RocketListUiState>(RocketListUiState.Loading)
    val uiState: StateFlow<RocketListUiState> = _uiState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private var allRockets: List<Rocket> = emptyList()

    init {
        loadRockets()
    }

    fun loadRockets() {
        viewModelScope.launch {
            repository.getRockets().collect { result ->
                _uiState.value = when (result) {
                    is Result.Loading -> RocketListUiState.Loading
                    is Result.Success -> {
                        allRockets = result.data
                        filterRockets(_searchQuery.value)
                        RocketListUiState.Success(result.data)
                    }
                    is Result.Error -> RocketListUiState.Error(result.message)
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterRockets(query)
    }

    private fun filterRockets(query: String) {
        val filtered = if (query.isEmpty()) {
            allRockets
        } else {
            allRockets.filter { rocket ->
                rocket.name.contains(query, ignoreCase = true)
            }
        }

        _uiState.value = if (filtered.isEmpty() && query.isNotEmpty()) {
            RocketListUiState.Empty
        } else {
            RocketListUiState.Success(filtered)
        }
    }
}

sealed class RocketListUiState {
    object Loading : RocketListUiState()
    object Empty : RocketListUiState()
    data class Success(val rockets: List<Rocket>) : RocketListUiState()
    data class Error(val message: String) : RocketListUiState()
}