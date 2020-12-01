package com.arts.amanda.service

import com.arts.amanda.utils.Constants.ARTS_COLLECTION
import com.arts.data.Arts
import com.arts.data.DataState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.io.IOException


class FirebaseService {

    private val dataStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private val dataDocuments: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val artsCollection = dataDocuments.collection(ARTS_COLLECTION)

    suspend fun get(): Flow<DataState<List<Arts>>> = flow {
        emit(DataState.Loading)
        try {
            val data = artsCollection.get().await().toObjects(Arts::class.java)
            for (datas in data) {
                emit(DataState.Success<List<Arts>>(datas.description.toString()))
            }
        } catch (exception: IOException) {
            emit(DataState.Error(exception))
        }
    }
}