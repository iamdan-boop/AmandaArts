package com.arts.amanda.service

import android.net.Uri
import androidx.core.net.toFile
import com.arts.amanda.data.Arts
import com.arts.amanda.data.DataState
import com.arts.amanda.utils.Constants.ARTS_COLLECTION
import com.arts.amanda.utils.Constants.ARTS_COLLECTION_CHILD
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException


class FirebaseService {

    private val dataStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private val dataStorageReference = dataStorage.reference.child(ARTS_COLLECTION_CHILD)
    private val dataDocuments: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val artsCollection = dataDocuments.collection(ARTS_COLLECTION)
    private val query = artsCollection.orderBy("title", Query.Direction.ASCENDING)

    fun getQueries() = query

    suspend fun get(): Flow<DataState<List<Arts>>> = flow {
        emit(DataState.Loading)
        try {
            val data = artsCollection.get().await().toObjects(Arts::class.java)
            emit(DataState.Success(data))
        } catch (exception: IOException) {
            emit(DataState.Error(exception))
        }
    }

    suspend fun uploadArt(file: Uri): String {
        return try {
            val id = file.toFile().name
            val data =
                dataStorageReference.child(id).putFile(file).await().storage.downloadUrl.await()
            data.toString()
        } catch (exception: FirebaseException) {
            exception.toString()
        }
    }

    suspend fun uploadArts(arts: Arts) {
        try {
            artsCollection.document(arts.title!!).set(arts).await()
        } catch (exception: Exception) {
            println("INSERT EXCEPTION: ${exception.localizedMessage}")
        }
    }
}