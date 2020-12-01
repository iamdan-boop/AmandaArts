package com.arts.amanda.data

sealed class DataState<out R> {

    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val error: Any) : DataState<Nothing>()
    object Loading : DataState<Nothing>()

}