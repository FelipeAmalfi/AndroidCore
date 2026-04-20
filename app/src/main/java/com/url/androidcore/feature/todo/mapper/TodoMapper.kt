package com.url.androidcore.feature.todo.mapper

import com.url.androidcore.core.time.DateFormatter
import com.url.androidcore.feature.todo.data.dto.TodoDto
import com.url.androidcore.feature.todo.domain.model.TodoItem
import com.url.androidcore.feature.todo.presentation.model.TodoUiItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun TodoDto.toDomain(): TodoItem = TodoItem(
    checked = checked,
    description = description,
    endDate = DateFormatter.fromIso8601(endDate)
)

fun TodoItem.toUi(): TodoUiItem = TodoUiItem(
    checked = checked,
    description = description,
    endDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(endDate))
)
