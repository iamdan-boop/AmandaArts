package com.arts.data

sealed class DataState<out R> {

    data class Success<out T>(val data: Any) : DataState<T>()
    data class Error(val error: Exception) : DataState<Nothing>()
    object Loading : DataState<Nothing>()

}