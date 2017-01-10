package com.angeloparenteapp.italianrecipes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by angel on 28/12/2016.
 */

public class FavouriteDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favourite.db";

    public FavouriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE " + RecipesContract.FavouriteEntry.TABLE_NAME + " (" +
                RecipesContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY," +
                RecipesContract.FavouriteEntry.COLUMN_RECIPES_TYPE + " TEXT NOT NULL, " +
                RecipesContract.FavouriteEntry.COLUMN_RECIPES_IMAGE + " TEXT NOT NULL, " +
                RecipesContract.FavouriteEntry.COLUMN_RECIPES_INGREDIENTS + " TEXT NOT NULL, " +
                RecipesContract.FavouriteEntry.COLUMN_RECIPES_PROCESS + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipesContract.FavouriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
