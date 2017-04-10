package study.pmoreira.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import study.pmoreira.popularmovies.data.MovieContract.FavMovieEntry;

class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAV_MOVIE_TABLE = "CREATE TABLE " + FavMovieEntry.TABLE_NAME + " (" +
                FavMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                FavMovieEntry.COLUMN_TITLE + " VARCHAR NOT NULL, " +
                FavMovieEntry.COLUMN_RATING + " REAL NOT NULL," +
                FavMovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
                FavMovieEntry.COLUMN_SYNOPSIS + " VARCHAR NOT NULL, " +
                FavMovieEntry.COLUMN_RUNTIME + " INTEGER NOT NULL DEFAULT 0, " +
                FavMovieEntry.COLUMN_POSTER_PATH + " VARCHAR NOT NULL); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAV_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}