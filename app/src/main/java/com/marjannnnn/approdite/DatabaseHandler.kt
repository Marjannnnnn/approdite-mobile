import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.marjannnnn.approdite.Project

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "Approdite"
        private const val TABLE_NAME = "project"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_TASK = "task"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_TASK TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addData(name: String, task: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_TASK, task)
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
                    cursor.getString(cursor.getColumnIndex(KEY_TASK))
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
        db.update(TABLE_NAME, values, "$KEY_ID = ?", arrayOf(project.id.toString()))
        db.close()
    }
    fun deleteData(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}
