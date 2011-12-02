
package com.reindeermobile.hunapknotifier.providers;

import com.reindeermobile.hunapknotifier.database.DataBaseHelper;
import com.reindeermobile.hunapknotifier.providers.HunApkInfoItem.HunApkInfoItems;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

public class HunApkProvider extends ContentProvider {
    private static final String DATABASE_NAME = "hunapkinfos";

    public static final String LOG_DATABASE = DataBaseHelper.LOG_TAG_DATABASE;

    public static final String PROVIDER_NAME = "com.reindeermobile.hunapknotifier.providers.hunapkprovider";

    private static HashMap<String, String> hunApkInfoProjectionMap;
    private DataBaseHelper dataBaseHelper;
    // private SQLiteDatabase database;

    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PROVIDER_NAME, HunApkInfoItems.HUNAPKINFO_TABLE_NAME,
                HunApkInfoItems.HUNAPKINFOS);
        URI_MATCHER.addURI(PROVIDER_NAME, HunApkInfoItems.HUNAPKINFO_TABLE_NAME + "/#",
                HunApkInfoItems.HUNAPKINFOS_ID);

        hunApkInfoProjectionMap = new HashMap<String, String>();
        hunApkInfoProjectionMap.put(HunApkInfoItems.ID, HunApkInfoItems.ID);
        hunApkInfoProjectionMap.put(HunApkInfoItems.NAME, HunApkInfoItems.NAME);
        hunApkInfoProjectionMap.put(HunApkInfoItems.LINK, HunApkInfoItems.LINK);
        hunApkInfoProjectionMap.put(HunApkInfoItems.AUTHOR, HunApkInfoItems.AUTHOR);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        Log.d(DataBaseHelper.LOG_TAG_DATABASE, "HunApkProvider.query: START");
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {
            case HunApkInfoItems.HUNAPKINFOS:
                qb.setTables(HunApkInfoItems.HUNAPKINFO_TABLE_NAME);
                qb.setProjectionMap(hunApkInfoProjectionMap);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        Log.d(DataBaseHelper.LOG_TAG_DATABASE, "HunApkProvider.query: END");
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues argValues) {
        Log.d(DataBaseHelper.LOG_TAG_DATABASE, "HunApkProvider.insert: START");
        if (URI_MATCHER.match(uri) != HunApkInfoItems.HUNAPKINFOS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (argValues != null) {
            values = new ContentValues(argValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        long rowId = db.insert(HunApkInfoItems.HUNAPKINFO_TABLE_NAME, HunApkInfoItems.NAME, values);
        // TODO ez így elég kicsavart...
        if (rowId > 0) {
            Uri userUri = ContentUris.withAppendedId(HunApkInfoItems.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(userUri, null);
            Log.d(DataBaseHelper.LOG_TAG_DATABASE, "HunApkProvider.insert: END");
            return userUri;
        }

        throw new SQLException("Failed to insert row into " + uri);

    }

    @Override
    public String getType(Uri uri) {
        Log.d(DataBaseHelper.LOG_TAG_DATABASE, "HunApkProvider.getType: START");
        Log.d(DataBaseHelper.LOG_TAG_DATABASE, "HunApkProvider.getType: END");
        switch (URI_MATCHER.match(uri)) {
            case HunApkInfoItems.HUNAPKINFOS:
                return HunApkInfoItems.CONTENT_TYPE;
            case HunApkInfoItems.HUNAPKINFOS_ID:
                return HunApkInfoItems.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        Log.d(DataBaseHelper.LOG_TAG_DATABASE, "HunApkProvider.onCreate: START");
        dataBaseHelper = new DataBaseHelper(getContext(), DATABASE_NAME, 1);
        Log.d(DataBaseHelper.LOG_TAG_DATABASE, "HunApkProvider.onCreate: END");
        return (dataBaseHelper != null) ? true : false;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

}
