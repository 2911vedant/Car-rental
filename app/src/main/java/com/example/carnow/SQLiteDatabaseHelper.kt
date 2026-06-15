package com.example.carnow
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// Database constants
private const val DATABASE_NAME = "CarRentalDB"
private const val DATABASE_VERSION = 1

// User table constants
private const val TABLE_USERS = "users"
private const val COLUMN_ID = "id"
private const val COLUMN_EMAIL = "email"
private const val COLUMN_PASSWORD = "password"

class SQLiteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // SQL query to create the users table
    private val CREATE_USERS_TABLE = ("CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_EMAIL + " TEXT UNIQUE,"
            + COLUMN_PASSWORD + " TEXT" + ")")

    override fun onCreate(db: SQLiteDatabase) {
        // Execute the table creation SQL
        db.execSQL(CREATE_USERS_TABLE)
        Log.d("DB", "Users table created successfully.")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if exists and re-create
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    /**
     * Inserts a new user into the database.
     * @return true if insertion was successful, false otherwise (e.g., email already exists).
     */
    fun addUser(email: String, passwordHash: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, passwordHash)
        }

        // Inserting Row. insert() returns the row ID of the newly inserted row, or -1 if an error occurred
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    /**
     * Checks if a user exists with the given email and password.
     * @return true if the credentials match, false otherwise.
     */
    fun checkUser(email: String, passwordHash: String): Boolean {
        val db = this.readableDatabase
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, passwordHash)

        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            selection,
            selectionArgs,
            null, null, null
        )

        val userExists = cursor.count > 0
        cursor.close()
        db.close()
        return userExists
    }

    /**
     * Updates the password for a given user email.
     * @return true if the update was successful, false otherwise.
     */
    fun updatePassword(email: String, newPasswordHash: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PASSWORD, newPasswordHash)
        }

        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        // update() returns the number of rows affected
        val count = db.update(TABLE_USERS, values, selection, selectionArgs)
        db.close()
        return count > 0
    }
}