package com.arts.amanda.ui.tabs.upload

import android.net.Uri
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.arts.amanda.data.Arts

import com.arts.amanda.service.FirebaseService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UploadViewModel
@ViewModelInject
constructor(
    private val service: FirebaseService,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _dataUrl: MutableLiveData<String> = MutableLiveData()
    val imageUrl: LiveData<String> get() = _dataUrl

    private val _dataStateUpload: MutableStateFlow<UploadState> =
        MutableStateFlow(UploadState.Initial)
    val stateUploadState: StateFlow<UploadState> get() = _dataStateUpload

    fun uploadImage(imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = service.uploadArt(imageUri)
            withContext(Dispatchers.Main) {
                _dataUrl.value = data
            }
        }
    }

    fun uploadArts(arts: Arts) = viewModelScope.launch {
        if (arts.title.isNullOrEmpty() || arts.collection.isNullOrEmpty() ||
            arts.date.isNullOrEmpty() || arts.description.isNullOrEmpty() || arts.image.isNullOrEmpty()
        ) {
            _dataStateUpload.value = UploadState.Error("Fields cannot be empty!")
        } else {
            withContext(Dispatchers.IO) {
                service.uploadArts(arts)
                withContext(Dispatchers.Main) {
                    _dataStateUpload.value = UploadState.Success(true)
                }
            }
        }
        _dataStateUpload.value = UploadState.Initial
    }
}

sealed class UploadState {

    data class Success(val isSuccess: Boolean) : UploadState()
    data class Error(val error: String) : UploadState()
    object Initial : UploadState()
}
