package study.pmoreira.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import study.pmoreira.popularmovies.data.MovieContract.FavMovieEntry;

public class MovieDao extends BaseDao {

    public MovieDao(Context context) {
        super(context);
    }

    public Cursor findAll(String[] projection, String sortOrder) {
        return query(FavMovieEntry.CONTENT_URI, projection, null, null, sortOrder);
    }

    public Uri insert(ContentValues contentValues) {
        return insert(FavMovieEntry.CONTENT_URI, contentValues);
    }

    public int delete(String where, String[] selectionArgs) {
        return delete(FavMovieEntry.CONTENT_URI, where, selectionArgs);
    }

    public boolean exists(String title) {
        Cursor cursor = query(FavMovieEntry.CONTENT_URI,
                new String[]{FavMovieEntry._ID},
                FavMovieEntry.WHERE_TITLE_EQUALS,
                new String[]{title},
                null);

        return cursor != null && cursor.moveToFirst();
    }
}
