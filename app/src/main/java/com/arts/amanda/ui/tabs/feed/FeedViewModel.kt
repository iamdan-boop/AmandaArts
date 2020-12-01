package com.arts.amanda.ui.tabs.feed

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arts.amanda.data.Arts
import com.arts.amanda.data.DataState
import com.arts.amanda.service.FirebaseService
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FeedViewModel
@ViewModelInject
constructor(
    private val service: FirebaseService
): ViewModel() {

    private val _dataState: MutableLiveData<DataState<List<Arts>>> = MutableLiveData()
    val artsState: LiveData<DataState<List<Arts>>> get() = _dataState

    fun setArtsStateEvent(stateEvent: ArtsStateEvent) {
        viewModelScope.launch {
            when (stateEvent) {
                ArtsStateEvent.GetArtsEvent -> {
                    service.get()
                        .onEach {
                            _dataState.value = it
                        }.launchIn(viewModelScope)
                }

                ArtsStateEvent.Nothing -> {
                    // DO SOME SHIT
                }
            }
        }
    }
}

sealed class ArtsStateEvent {
    object GetArtsEvent: ArtsStateEvent()
    object Nothing: ArtsStateEvent()
}