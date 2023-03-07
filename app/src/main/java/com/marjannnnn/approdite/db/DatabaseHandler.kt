package com.marjannnnn.approdite.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.marjannnnn.approdite.model.Project

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "Approdite"
        private const val TABLE_NAME = "project"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_TASK = "task"
        private const val KEY_ASSIGN_TO = "assign_to"
        private const val KEY_SPRINT = "sprint"
        private const val KEY_START_DATE = "start_date"
        private const val KEY_END_DATE = "end_date"
        private const val KEY_ATTACHMENT = "attachment"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_TASK TEXT, $KEY_ASSIGN_TO TEXT, $KEY_SPRINT TEXT, $KEY_START_DATE TEXT, $KEY_END_DATE TEXT, $KEY_ATTACHMENT TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addData(
        name: String,
        task: String,
        assignTo: String,
        sprint: String,
        startDate: String,
        endDate: String,
        attachment: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_TASK, task)
        values.put(KEY_ASSIGN_TO, assignTo)
        values.put(KEY_SPRINT, sprint)
        values.put(KEY_START_DATE, startDate)
        values.put(KEY_END_DATE, endDate)
        values.put(KEY_ATTACHMENT, attachment)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllData(): ArrayList<Project> {
        val projectList = ArrayList<Project>()
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val project = Project(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_TASK)),
                    cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_TO)),
                    cursor.getString(cursor.getColumnIndex(KEY_SPRINT)),
                    cursor.getString(cursor.getColumnIndex(KEY_START_DATE)),
                    cursor.getString(cursor.getColumnIndex(KEY_END_DATE)),
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
        values.put(KEY_START_DATE, project.startDate)
        values.put(KEY_END_DATE, project.endDate)
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
