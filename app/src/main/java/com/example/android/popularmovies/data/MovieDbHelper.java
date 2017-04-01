package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PallaviNishanth on 3/14/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVMOVIE_TABLE = " CREATE TABLE IF NOT EXISTS " +
                MovieContract.MovieEntry.TABLE_NAME +
                "(" +
                MovieContract.MovieEntry._ID + " INTEGER," +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY," +
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_RELEASEDATE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_VOTEAVERAGE + " REAL NOT NULL" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVMOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
