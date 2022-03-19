package com.iamcheng5.firestorerecyclerviewdemo_mvvm

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(@DocumentId val id: String,val title:String ,val description: String, val content: String):
    Parcelable {
    @Suppress("unused")
    constructor() : this("","", "", "")
}