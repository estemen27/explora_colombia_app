package com.estebanbe.exploracolombiaapp.service

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseService {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val realtimeDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    // Firestore Operations
    fun saveToFirestore(collection: String, document: String, data: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection(collection).document(document)
            .set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun addToFirestoreCollection(collection: String, data: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection(collection).add(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Realtime Database Operations
    fun saveToRealtimeDatabase(path: String, key: String?, data: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val ref = if (key != null) realtimeDatabase.child(path).child(key) else realtimeDatabase.child(path).push()
        ref.setValue(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Firebase Storage Operations
    fun uploadToStorage(path: String, data: ByteArray, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val ref: StorageReference = storage.reference.child(path)
        ref.putBytes(data)
            .addOnSuccessListener { taskSnapshot ->
                ref.downloadUrl.addOnSuccessListener { uri -> onSuccess(uri.toString()) }
            }
            .addOnFailureListener { onFailure(it) }
    }

    // FirebaseService.kt
    fun getFromStorage(path: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val ref: StorageReference = storage.reference.child(path)
        ref.downloadUrl
            .addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}