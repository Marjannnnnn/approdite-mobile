package com.marjannnnn.approdite.model

data class Project(
    val id: Int,
    val projectName: String,
    val taskName: String,
    val assignTo: String,
    val sprint: String,
    val startDate: String,
    val endDate: String,
    val attachment: String
)
