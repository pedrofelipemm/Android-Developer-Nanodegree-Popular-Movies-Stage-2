package study.pmoreira.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

class BaseDao {

    private final Context mContext;

    BaseDao(final Context context) {
        mContext = context;
    }

    Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    Uri insert(Uri uri, ContentValues contentValues) {
        return mContext.getContentResolver().insert(uri, contentValues);
    }

    int delete(Uri uri, String where, String[] selectionArgs) {
        return mContext.getContentResolver().delete(uri, where, selectionArgs);
    }

}
