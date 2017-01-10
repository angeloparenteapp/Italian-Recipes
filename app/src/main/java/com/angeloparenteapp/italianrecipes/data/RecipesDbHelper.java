package com.angeloparenteapp.italianrecipes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.angeloparenteapp.italianrecipes.data.RecipesContract.RecipesEntry;

/**
 * Created by angel on 17/12/2016. DataBase
 */

public class RecipesDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "recipes.db";

    public RecipesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE " + RecipesEntry.TABLE_NAME + " (" +
                RecipesEntry._ID + " INTEGER PRIMARY KEY," +
                RecipesEntry.COLUMN_RECIPES_TYPE + " TEXT NOT NULL, " +
                RecipesEntry.COLUMN_RECIPES_IMAGE + " TEXT NOT NULL, " +
                RecipesEntry.COLUMN_RECIPES_INGREDIENTS + " TEXT NOT NULL, " +
                RecipesEntry.COLUMN_RECIPES_PROCESS + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
