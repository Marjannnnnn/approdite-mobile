package com.marjannnnn.approdite.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.marjannnnn.approdite.model.Project
import java.text.SimpleDateFormat
import java.util.Locale

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "Approdite"
        private const val TABLE_NAME = "project"
        private const val KEY_ID = "id"
        private const val KEY_PROJECT_NAME = "projectName"
        private const val KEY_TASK_NAME = "taskName"
        private const val KEY_ASSIGN_TO = "assignTo"
        private const val KEY_SPRINT = "sprint"
        private const val KEY_START_DATE = "startDate"
        private const val KEY_END_DATE = "endDate"
        private const val KEY_ATTACHMENT = "attachment"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME($KEY_ID INTEGER PRIMARY KEY, $KEY_PROJECT_NAME TEXT, $KEY_TASK_NAME TEXT, $KEY_ASSIGN_TO TEXT, $KEY_SPRINT INTEGER, $KEY_START_DATE TEXT, $KEY_END_DATE TEXT, $KEY_ATTACHMENT TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addData(project: Project) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_PROJECT_NAME, project.projectName)
        values.put(KEY_TASK_NAME, project.taskName)
        values.put(KEY_ASSIGN_TO, project.assignTo)
        values.put(KEY_SPRINT, project.sprint)
        values.put(
            KEY_START_DATE,
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(project.startDate)
        )
        values.put(
            KEY_END_DATE,
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(project.endDate)
        )
        values.put(KEY_ATTACHMENT, project.attachment)
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
                val project = Project(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROJECT_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_TASK_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_ASSIGN_TO)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SPRINT)),
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(KEY_START_DATE)
                        )
                    )!!,
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(KEY_END_DATE)
                        )
                    )!!,
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_ATTACHMENT))
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
        values.put(KEY_PROJECT_NAME, project.projectName)
        values.put(KEY_TASK_NAME, project.taskName)
        values.put(KEY_ASSIGN_TO, project.assignTo)
        values.put(KEY_SPRINT, project.sprint)
        values.put(KEY_START_DATE, project.startDate.time)
        values.put(KEY_END_DATE, project.endDate.time)
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
