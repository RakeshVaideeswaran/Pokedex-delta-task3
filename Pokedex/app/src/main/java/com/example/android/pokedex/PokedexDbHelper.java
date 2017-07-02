package com.example.android.pokedex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.version;

/**
 * Created by rakesh on 6/29/2017.
 */

public class PokedexDbHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME = "Pokedex.db";
    private static final int DATABASEVERSION = 3;

    public PokedexDbHelper(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATETABLE = "CREATE TABLE "+
                PokedexContract.PokedexEntry.TABLENAME + " (" +
                PokedexContract.PokedexEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PokedexContract.PokedexEntry.NAME_COLUMN + " TEXT," +
                PokedexContract.PokedexEntry.IMAGE_COLUMN + " TEXT" +
                ");";

        db.execSQL(CREATETABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + PokedexContract.PokedexEntry.TABLENAME);
        onCreate(db);
    }
}
