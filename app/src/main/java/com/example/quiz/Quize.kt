package com.example.quiz

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import org.apache.commons.lang3.mutable.Mutable

data class Quize(
    val id: Int,
    val question: String,
    val answers: Int,
    val choices: MutableList<String>
)