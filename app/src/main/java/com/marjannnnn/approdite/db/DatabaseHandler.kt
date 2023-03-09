package com.marjannnnn.approdite.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.marjannnnn.approdite.model.Project
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "Approdite"
        private const val TABLE_NAME = "project"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "projectName"
        private const val KEY_TASK = "taskName"
        private const val KEY_ASSIGN_TO = "assignTo"
        private const val KEY_SPRINT = "sprint"
        private const val KEY_START_DATE = "startDate"
        private const val KEY_END_DATE = "endDate"
        private const val KEY_ATTACHMENT = "attachment"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_TASK TEXT, $KEY_ASSIGN_TO TEXT, $KEY_SPRINT INTEGER, $KEY_START_DATE DATE, $KEY_END_DATE DATE, $KEY_ATTACHMENT TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addData(name: String, task: String, assignTo: String, sprint: Int, startDate: Date, endDate: Date, attachment: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        val startDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(startDate)
        val endDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(endDate)

        Log.i("start date:","${startDate}")
        Log.i("end date:", "${endDate}")

        // end date: Mon Mar 20 16:42:31 GMT+07:00 2023
        // start date:  Mon Mar 20 16:42:31 GMT+07:00 2023

        values.put(KEY_NAME, name)
        values.put(KEY_TASK, task)
        values.put(KEY_ASSIGN_TO, assignTo)
        values.put(KEY_SPRINT, sprint)
        values.put(KEY_START_DATE, startDateString)
        values.put(KEY_END_DATE, endDateString)
        values.put(KEY_ATTACHMENT, attachment)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllData(): ArrayList<Project> {
        val projectList = ArrayList<Project>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val startDateString = cursor.getString(cursor.getColumnIndex(KEY_START_DATE))
                val endDateString = cursor.getString(cursor.getColumnIndex(KEY_END_DATE))
                val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(startDateString)
                val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(endDateString)
                val project = Project(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_TASK)),
                    cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_TO)),
                    cursor.getInt(cursor.getColumnIndex(KEY_SPRINT)),
                    startDate,
                    endDate,
                    cursor.getString(cursor.getColumnIndex(KEY_ATTACHMENT))
                )
                projectList.add(project)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return projectList
    }

    fun updateData(project: Project) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, project.projectName)
        values.put(KEY_TASK, project.taskName)
        values.put(KEY_ASSIGN_TO, project.assignTo)
        values.put(KEY_SPRINT, project.sprint)
        values.put(KEY_START_DATE, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(project.startDate))
        values.put(KEY_END_DATE, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(project.endDate))
        values.put(KEY_ATTACHMENT, project.attachment)
        db.update(TABLE_NAME, values, "$KEY_ID = ?", arrayOf(project.id.toString()))
        db.close()
    }


    fun deleteData(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}