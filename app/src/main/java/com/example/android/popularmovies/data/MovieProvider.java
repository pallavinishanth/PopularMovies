package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by PallaviNishanth on 3/15/17.
 */

public class MovieProvider extends ContentProvider {

    static final int MOVIES = 100;

    static final int MOVIES_WITH_MOVIEID = 101;

    private static final SQLiteQueryBuilder sMovieQBuilder;

    static{

        sMovieQBuilder = new SQLiteQueryBuilder();
        sMovieQBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME);
    }

    // The URI Matcher used by this content Provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MovieDbHelper mOpenHelper;

    static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIES_WITH_MOVIEID);

        return matcher;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {

            case MOVIES:
                return MovieContract.MovieEntry.CONTENT_TYPE;

            case MOVIES_WITH_MOVIEID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {

            case MOVIES:

                //db.beginTransaction();
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);

                if(_id >0){

                    returnUri = MovieContract.MovieEntry.buildFavMovieUri(_id);
                }else

                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        //db.close();
        return returnUri;

    }

    @Override
    public boolean onCreate() {

        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch(match){

            case MOVIES_WITH_MOVIEID:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case MOVIES:

                retCursor = sMovieQBuilder.query(mOpenHelper.getReadableDatabase(), projection,
                        selection, selectionArgs, null, null, sortOrder);

                break;
            case MOVIES_WITH_MOVIEID:

                retCursor = sMovieQBuilder.query(mOpenHelper.getReadableDatabase(), projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return retCursor;
    }
}
