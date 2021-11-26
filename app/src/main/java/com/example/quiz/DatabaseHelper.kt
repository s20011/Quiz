package com.example.quiz

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,
    null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "quize.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sb = """
            CREATE TABLE quize (
            _id INTEGER PRIMARY KEY,
            question TEXT,
            answers INTEGER,
            choicei TEXT, 
            choiceii TEXT,
            choiceiii TEXT,
            choiceiv TEXT,
            choicev TEXT,
            choicevi TEXT
            )
        """.trimIndent()

        db!!.execSQL(sb)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}