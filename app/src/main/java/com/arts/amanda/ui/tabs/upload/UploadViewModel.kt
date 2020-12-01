package com.arts.amanda.ui.tabs.upload

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.arts.amanda.service.FirebaseService
import com.arts.data.Arts
import com.arts.data.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class UploadViewModel
@ViewModelInject
constructor(
    private val service: FirebaseService,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _dataState: MutableLiveData<DataState<List<Arts>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<Arts>>>
        get() = _dataState

    fun setViewState(artsStateEvent: ArtsStateEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (artsStateEvent) {

                is ArtsStateEvent.GetArtsEvent -> {
                    service.get()
                        .onEach {
                            _dataState.value = it
                    }.launchIn(viewModelScope)
                }

                is ArtsStateEvent.Nothing -> {
                    // DO NOTHING OR ELSE
                }
            }
        }
    }
}

sealed class ArtsStateEvent {
    object GetArtsEvent: ArtsStateEvent()
    object Nothing: ArtsStateEvent()
}

