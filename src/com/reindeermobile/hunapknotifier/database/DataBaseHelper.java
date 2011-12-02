
package com.reindeermobile.hunapknotifier.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG_DATABASE = "database";
    private static final String CREATE_SCRIPT = "create.sql";
    // private static final String INSERT_SCRIPT = "insert.sql";
    private Context context;

    public DataBaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG_DATABASE, "DataBaseHelper.onCreate - v" + db.getVersion());
        Log.i(LOG_TAG_DATABASE, "DataBaseHelper.onCreate - create tables: " + CREATE_SCRIPT);
        loadSqlFile(db, CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(LOG_TAG_DATABASE, "DataBaseHelper.onUpgrade - v" + db.getVersion() + " from:"
                + oldVersion + " to:" + newVersion);
    }

    private void loadSqlFile(final SQLiteDatabase db, String sqlFileName) {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        String line = "";
        try {
            inputStream = getInputStreamFromAssets(context, sqlFileName);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() > 0) {
                    Log.d(LOG_TAG_DATABASE, "loadSqlFile: loaded line - " + line);
                    db.execSQL(line);
                }
            }
        } catch (IOException e) {
            Log.w(LOG_TAG_DATABASE, "IO error during ");
        } catch (SQLException e) {
            Log.w(LOG_TAG_DATABASE, e.getMessage() + "(" + line + ")", e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.w(LOG_TAG_DATABASE, "Can't close buffered reader: ");
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.w(LOG_TAG_DATABASE, "Can't close buffered reader: ");
                }
            }
        }
    }

    public static InputStream getInputStreamFromAssets(Context context, String path)
            throws IOException {
        InputStream inputStream = null;
        inputStream = context.getAssets().open(path);
        return inputStream;
    }

}
