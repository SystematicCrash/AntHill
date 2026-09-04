package com.anthill.app.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anthill.app.data.dao.LocationDao
import com.anthill.app.data.model.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel(private val locationDao: LocationDao) : ViewModel() {
    private val _viewState = MutableStateFlow<LocationViewState>(LocationViewState.Loading)
    val viewState: StateFlow<LocationViewState> = _viewState

    fun loadRootLocations() {
        viewModelScope.launch {
            _viewState.value = LocationViewState.Loading
            try {
                val roots = locationDao.getRootLocations()
                _viewState.value = LocationViewState.Success(roots, null)
            } catch (e: Exception) {
                _viewState.value = LocationViewState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadChildren(parent: Location) {
        viewModelScope.launch {
            _viewState.value = LocationViewState.Loading
            try {
                val children = locationDao.getChildren(parent.id)
                _viewState.value = LocationViewState.Success(children, parent)
            } catch (e: Exception) {
                _viewState.value = LocationViewState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class LocationViewState {
    object Loading : LocationViewState()
    data class Success(val locations: List<Location>, val parent: Location?) : LocationViewState()
    data class Error(val message: String) : LocationViewState()
}

class LocationViewModelFactory(private val locationDao: LocationDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationViewModel(locationDao) as T
    }
}
