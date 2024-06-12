package com.example.parrot.data

import com.example.parrot.core.type.ErrorType
import com.example.parrot.domain.FireStoreRepository
import com.example.parrot.domain.model.NotesModel
import com.example.parrot.domain.state.NotesState
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class FireStoreRepositoryImpl @Inject constructor(
    firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore
) : FireStoreRepository {

    private val userId = firebaseAuth.currentUser?.uid

    override suspend fun getNotes(): Task<NotesState> {
        val documentRef = firebaseFireStore.collection("notes")
            .whereEqualTo("userId", userId)
        return getNotesFromFireStore(documentRef)
    }

    override suspend fun getNoteId(noteId: String): Task<NotesState> {
        val documentRef = firebaseFireStore.collection("notes")
            .whereEqualTo("userId", userId)
            .whereEqualTo("id", noteId)
        return getNotesFromFireStore(documentRef)
    }

    private fun getNotesFromFireStore(documentRef: Query): Task<NotesState> {
        return documentRef.get().continueWith { task ->
            if (task.isSuccessful) {
                val notesList = mutableListOf<NotesModel>()
                if (!task.result.isEmpty) {
                    for (data in task.result.documents) {
                        val notesModel: NotesModel? = data.toObject(NotesModel::class.java)
                        if (notesModel != null) {
                            notesList.add(notesModel)
                        }
                    }
                }
                NotesState.Success(notesList)
            } else {
                throw Exception(ErrorType.GetNotes.errorMessage.toString())
            }
        }
    }

    override suspend fun saveNote(notesResponse: NotesModel): Task<NotesState> {
        notesResponse.userId = userId.toString()
        val documentRef = firebaseFireStore.collection("notes")
        val idRef = documentRef.document().id
        notesResponse.id = idRef
        return documentRef.document(idRef).set(notesResponse).continueWith { task ->
            if (task.isSuccessful) {
                NotesState.Complete
            } else {
                throw Exception(ErrorType.SaveNotes.errorMessage.toString())
            }
        }
    }

    override suspend fun updateNote(notesResponse: NotesModel): Task<NotesState> {
        val documentRef = firebaseFireStore.collection("notes").document(notesResponse.id!!)
        return documentRef.set(notesResponse).continueWith { task ->
            if (task.isSuccessful) {
                NotesState.Complete
            } else {
                throw Exception(ErrorType.UpdateNotes.errorMessage.toString())
            }
        }
    }

    override suspend fun deleteNote(noteId: String): Task<NotesState> {
        val documentRef = firebaseFireStore.collection("notes").document(noteId)
        return documentRef.delete().continueWith { task ->
            if (task.isSuccessful) {
                NotesState.Complete
            } else {
                throw Exception(ErrorType.DeleteNotes.errorMessage.toString())
            }
        }
    }

    override suspend fun multiUpdateNote(
        listNoteId: MutableList<String>,
        mapUpdate: Map<String, Boolean>
    ): Task<NotesState> {
        val batch = firebaseFireStore.batch()
        listNoteId.forEach { noteId ->
            val documentRef = firebaseFireStore.collection("notes").document(noteId)
            batch.update(documentRef, mapUpdate)
        }
        return batch.commit().continueWith { task ->
            if (task.isSuccessful) {
                NotesState.Complete
            } else {
                throw Exception(ErrorType.UpdateNotes.errorMessage.toString())
            }
        }
    }

    override suspend fun multiDeleteNote(listNoteId: MutableList<String>): Task<NotesState> {
        val batch = firebaseFireStore.batch()
        listNoteId.forEach { noteId ->
            val documentRef = firebaseFireStore.collection("notes").document(noteId)
            batch.delete(documentRef)
        }
        return batch.commit().continueWith { task ->
            if (task.isSuccessful) {
                NotesState.Complete
            } else {
                throw Exception(ErrorType.DeleteNotes.errorMessage.toString())
            }
        }
    }
}