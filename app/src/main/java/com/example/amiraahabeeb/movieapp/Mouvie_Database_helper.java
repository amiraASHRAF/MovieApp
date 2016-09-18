package com.example.amiraahabeeb.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Amira A. habeeb on 06/09/2016.
 */
public class Mouvie_Database_helper {

    DatabaseHelper databaseHelper;
    Context context;

    public Mouvie_Database_helper(Context context) {
        databaseHelper = new DatabaseHelper(context);
        this.context = context;
    }


    public long insertData(Mouvie_parsing mouvie_parsing) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.UID, mouvie_parsing.getid());
        contentValues.put(DatabaseHelper.POSTER_PATH, mouvie_parsing.getPoster_path());
        contentValues.put(DatabaseHelper.REVIEW, mouvie_parsing.getOverview());
        contentValues.put(DatabaseHelper.RELEASE_DATE, mouvie_parsing.getRelease_date());
        contentValues.put(DatabaseHelper.ORIGINAL_TITLE, mouvie_parsing.getOriginal_title());
        contentValues.put(DatabaseHelper.POPULARITY, mouvie_parsing.getPopularity());
        contentValues.put(DatabaseHelper.CONTENT, mouvie_parsing.getContent());


        long id = sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public ArrayList<Mouvie_parsing> getAllData() {
        int i = 0;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.POSTER_PATH, DatabaseHelper.REVIEW, DatabaseHelper.RELEASE_DATE, DatabaseHelper.ORIGINAL_TITLE, DatabaseHelper.POPULARITY, DatabaseHelper.CONTENT};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<Mouvie_parsing> mouvie_parsings = new ArrayList<Mouvie_parsing>();
        while (cursor.moveToNext()) {
            int ID = cursor.getInt(0);
            String POSTER_PATH = cursor.getString(1);
            String REVIEW = cursor.getString(2);
            String RELEASE_DATE = cursor.getString(3);
            String ORIGINAL_TITLE = cursor.getString(4);
            String POPULARITY = cursor.getString(5);
            String CONTENT = cursor.getString(6);

            mouvie_parsings.add(new Mouvie_parsing());
            mouvie_parsings.get(i).setPoster_path(POSTER_PATH);
            mouvie_parsings.get(i).setOverview(REVIEW);
            mouvie_parsings.get(i).setRelease_date(RELEASE_DATE);
            mouvie_parsings.get(i).setOriginal_title(ORIGINAL_TITLE);
            mouvie_parsings.get(i).setPopularity(POPULARITY);
            mouvie_parsings.get(i).setContent(CONTENT);
            i++;
        }
        return mouvie_parsings;
    }

    public int UpdateData(Mouvie_parsing OldmovieData, Mouvie_parsing NewmouvietData) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValuse = new ContentValues();
        contentValuse.put(DatabaseHelper.POSTER_PATH, NewmouvietData.getPoster_path());
        contentValuse.put(DatabaseHelper.REVIEW, NewmouvietData.getOverview());
        contentValuse.put(DatabaseHelper.RELEASE_DATE, NewmouvietData.getRelease_date());
        contentValuse.put(DatabaseHelper.ORIGINAL_TITLE, NewmouvietData.getOriginal_title());
        contentValuse.put(DatabaseHelper.POPULARITY, NewmouvietData.getPopularity());
        contentValuse.put(DatabaseHelper.CONTENT, NewmouvietData.getContent());
        String whereArgs[] = {OldmovieData.getid() + ""};
        int count = db.update(DatabaseHelper.TABLE_NAME, contentValuse,
                DatabaseHelper.UID + " =? ", whereArgs);
//        Toast.makeText(context, count, Toast.LENGTH_SHORT).show();
        return count;
    }

    public int DeleteData(Mouvie_parsing mouvieParsing) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String whereArgs[] = {mouvieParsing.getid() + ""};
        int count = db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.UID + " =?", whereArgs);
        return count;

    }


    static class DatabaseHelper extends SQLiteOpenHelper {
        private final static String DATABASE_NAME = "MouvieDatabase";
        private final static int DATABASE_VERSION = 1;
        private final static String TABLE_NAME = "mouvietable";
        private final static String UID = "_id";
        private final static String POSTER_PATH = "poster_path";
        private final static String REVIEW = "review";
        private final static String RELEASE_DATE = "release_date";
        private final static String ORIGINAL_TITLE = "original_title";
        private final static String POPULARITY = "popularity";
        private final static String CONTENT = "content";
        private final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + POSTER_PATH + " VARCHAR(255) ," + REVIEW + " VARCHAR(255) ," + RELEASE_DATE + " VARCHAR(255) ," + ORIGINAL_TITLE + " VARCHAR(255) ," + POPULARITY + " VARCHAR(255) ," + CONTENT + " VARCHAR(255) );";
        private final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE);
                Toast.makeText(context, "database CREATED", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(context, "Sorry there is Error in create your database \n" + e, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                sqLiteDatabase.execSQL(DROP_TABLE);
                onCreate(sqLiteDatabase);
            } catch (SQLException e) {
                Toast.makeText(context, "Sorry there is Error in upgrade your database \n" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

