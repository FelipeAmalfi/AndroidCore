package com.url.androidcore.feature.todo.data.dto

import com.google.gson.annotations.SerializedName

data class TodoDto(
    @SerializedName("checked") val checked: Boolean,
    @SerializedName("description") val description: String,
    @SerializedName("end_date") val endDate: String
)
