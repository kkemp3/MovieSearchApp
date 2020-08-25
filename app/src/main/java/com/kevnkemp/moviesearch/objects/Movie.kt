package com.kevnkemp.moviesearch.objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Movie(val imgLocation: String,
                 val name: String,
                 val date: String,
                 val desc: String,
                 val rating: String)