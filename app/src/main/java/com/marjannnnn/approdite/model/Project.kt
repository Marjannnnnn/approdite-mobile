package com.marjannnnn.approdite.model

import java.io.Serializable
import java.util.Date

data class Project(
    val id: Int,
    val projectName: String,
    val taskName: String,
    val assignTo: String,
    val sprint: Int,
    val startDate: Date,
    val endDate: Date,
    val attachment: String
): Serializable
