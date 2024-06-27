package com.example.lekhantu.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val noteTitle: String = " ",
    val noteDesc: String = " ",
    val noteCategory: String = " ",
    val date: String = " ",

    ) : Parcelable