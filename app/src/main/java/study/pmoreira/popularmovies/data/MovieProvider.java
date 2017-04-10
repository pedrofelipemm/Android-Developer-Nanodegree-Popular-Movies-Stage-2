package study.pmoreira.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import study.pmoreira.popularmovies.R;
import study.pmoreira.popularmovies.data.MovieContract.FavMovieEntry;

public class MovieProvider extends ContentProvider {

    private static final String TAG = MovieProvider.class.getName();

    public static final int COD_MOVIES = 100;
    public static final int COD_MOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mDbHelper;
    private Context mContext;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, COD_MOVIES);

        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", COD_MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDbHelper = new MovieDbHelper(mContext);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case COD_MOVIES:
                cursor = db.query(FavMovieEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case COD_MOVIE_ID:
                selection = FavMovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(FavMovieEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(mContext.getString(R.string.error_unknown_uri, uri));
        }

        cursor.setNotificationUri(mContext.getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COD_MOVIES:
                return insertItem(uri, values);
            default:
                throw new IllegalArgumentException(
                        mContext.getString(R.string.error_provider_operation_not_supported, "Insert", uri));
        }
    }

    private Uri insertItem(Uri uri, ContentValues values) {
        validateItem(values);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long rowId = db.insertWithOnConflict(FavMovieEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (rowId == -1) {
            Log.e(TAG, mContext.getString(R.string.error_insertion_failed, uri));
            return null;
        }

        mContext.getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, rowId);
    }

    private void validateItem(ContentValues values) {
        String title = values.getAsString(FavMovieEntry.COLUMN_TITLE);
        if (TextUtils.isEmpty(title.trim())) {
            throw new IllegalArgumentException(
                    mContext.getString(R.string.field_required, FavMovieEntry.COLUMN_TITLE));
        }

        Double rating = values.getAsDouble(FavMovieEntry.COLUMN_RATING);
        if (rating == null || rating < 0) {
            throw new IllegalArgumentException(
                    mContext.getString(R.string.field_required, FavMovieEntry.COLUMN_RATING));
        }

        String posterPath = values.getAsString(FavMovieEntry.COLUMN_POSTER_PATH);
        if (TextUtils.isEmpty(posterPath.trim())) {
            throw new IllegalArgumentException(
                    mContext.getString(R.string.field_required, FavMovieEntry.COLUMN_POSTER_PATH));
        }

        String releaseDate = values.getAsString(FavMovieEntry.COLUMN_RELEASE_DATE);
        if (TextUtils.isEmpty(releaseDate.trim())) {
            throw new IllegalArgumentException(
                    mContext.getString(R.string.field_required, FavMovieEntry.COLUMN_RELEASE_DATE));
        }

        String synopsis = values.getAsString(FavMovieEntry.COLUMN_SYNOPSIS);
        if (TextUtils.isEmpty(synopsis.trim())) {
            throw new IllegalArgumentException(
                    mContext.getString(R.string.field_required, FavMovieEntry.COLUMN_SYNOPSIS));
        }

        Integer runtime = values.getAsInteger(FavMovieEntry.COLUMN_RUNTIME);
        if (runtime == null || runtime < 0) {
            throw new IllegalArgumentException(
                    mContext.getString(R.string.field_required, FavMovieEntry.COLUMN_RUNTIME));
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int rowsUpdated;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COD_MOVIES:
                rowsUpdated = updateItem(uri, contentValues, selection, selectionArgs);
                break;
            case COD_MOVIE_ID:
                selection = FavMovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                rowsUpdated = updateItem(uri, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException(
                        mContext.getString(R.string.error_provider_operation_not_supported, "Update", uri));
        }

        return rowsUpdated;

    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }

        validateUpdate(values);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(FavMovieEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    private void validateUpdate(ContentValues values) {
        if (values.containsKey(FavMovieEntry.COLUMN_TITLE)) {
            String title = values.getAsString(FavMovieEntry.COLUMN_TITLE);
            if (TextUtils.isEmpty(title)) {
                throw new IllegalArgumentException(
                        mContext.getString(R.string.field_required, FavMovieEntry.COLUMN_TITLE));
            }
        }

        if (values.containsKey(FavMovieEntry.COLUMN_RELEASE_DATE)) {
            String releaseDate = values.getAsString(FavMovieEntry.COLUMN_RELEASE_DATE);
            if (TextUtils.isEmpty(releaseDate)) {
                throw new IllegalArgumentException(
                        mContext.getString(R.string.field_required, FavMovieEntry.COLUMN_RELEASE_DATE));
            }
        }

        if (values.containsKey(FavMovieEntry.COLUMN_RATING)) {
            Double rating = values.getAsDouble(FavMovieEntry.COLUMN_RATING);
            if (rating == null || rating < 0) {
                throw new IllegalArgumentException(
                        mContext.getString(R.string.field_required, FavMovieEntry.COLUMN_RATING));
            }
        }

        if (values.containsKey(FavMovieEntry.COLUMN_POSTER_PATH)) {
            String posterPath = values.getAsString(FavMovieEntry.COLUMN_POSTER_PATH);
            if (TextUtils.isEmpty(posterPath.trim())) {
                throw new IllegalArgumentException(
                        mContext.getString(R.string.field_required, FavMovieEntry.COLUMN_POSTER_PATH));
            }
        }

        if (values.containsKey(FavMovieEntry.COLUMN_SYNOPSIS)) {
            String synopsis = values.getAsString(FavMovieEntry.COLUMN_SYNOPSIS);
            if (TextUtils.isEmpty(synopsis.trim())) {
                throw new IllegalArgumentException(
                        mContext.getString(R.string.field_required, FavMovieEntry.COLUMN_SYNOPSIS));
            }
        }

        if (values.containsKey(FavMovieEntry.COLUMN_RUNTIME)) {
            Integer runtime = values.getAsInteger(FavMovieEntry.COLUMN_RUNTIME);
            if (runtime == null || runtime < 0) {
                throw new IllegalArgumentException(
                        mContext.getString(R.string.field_required, FavMovieEntry.COLUMN_RUNTIME));
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted = 0;
        if (selection == null) {
            return numRowsDeleted;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case COD_MOVIES:
                numRowsDeleted = db.delete(FavMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COD_MOVIE_ID:
                selection = FavMovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                numRowsDeleted = db.delete(FavMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(mContext.getString(R.string.error_unknown_uri, uri));
        }

        if (numRowsDeleted != 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COD_MOVIES:
                return FavMovieEntry.CONTENT_LIST_TYPE;
            case COD_MOVIE_ID:
                return FavMovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException(mContext.getString(R.string.error_unknown_uri, uri));
        }
    }
}