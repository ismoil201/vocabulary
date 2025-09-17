package com.ismoil.vocabulary.room.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "word")
data class Word(


    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val text: String,
    val mean: String,
    val type: String


) : Parcelable