package com.rahmanarifofficial.filmcatalog.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class FavoriteDbHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "Favorite.db", null, 1) {
    companion object {
        private var instance: FavoriteDbHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): FavoriteDbHelper {
            if (instance == null) {
                instance = FavoriteDbHelper(ctx.applicationContext)
            }
            return instance as FavoriteDbHelper
        }

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(
            FilmDB.TABLE_FILM, true,
            FilmDB.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FilmDB.KODE to TEXT + UNIQUE,
            FilmDB.TITLE to TEXT,
            FilmDB.YEAR to TEXT,
            FilmDB.TYPE to TEXT,
            FilmDB.POSTER to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(FilmDB.TABLE_FILM, true)
    }
}

val Context.database: FavoriteDbHelper
    get() = FavoriteDbHelper.getInstance(applicationContext)