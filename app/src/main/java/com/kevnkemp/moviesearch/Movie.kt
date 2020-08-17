package com.kevnkemp.moviesearch

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(val imgLocation: String, val name: String, val date: String, val desc: String) : Parcelable