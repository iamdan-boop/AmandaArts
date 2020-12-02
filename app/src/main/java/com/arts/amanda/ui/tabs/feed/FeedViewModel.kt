package com.arts.amanda.ui.tabs.feed

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arts.amanda.service.FirebaseService
import kotlinx.coroutines.launch

class FeedViewModel
@ViewModelInject
constructor(
    private val service: FirebaseService
): ViewModel() {


    fun deleteArt(title: String) = viewModelScope.launch {
        service.deleteArt(title)
    }
}
