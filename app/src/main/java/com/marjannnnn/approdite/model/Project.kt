package com.marjannnnn.approdite.model

import java.util.*

data class Project(
    val id: Int,
    val projectName: String,
    val taskName: String,
    val assignTo: String,
    val sprint: Int,
    val startDate: Date,
    val endDate: Date,
    val attachment: String
)
