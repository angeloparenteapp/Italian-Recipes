package com.angeloparenteapp.italianrecipes.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RecipesDbHelper mOpenHelper;
    private FavouriteDbHelper mFavouriteOpenHelper;
    static final int RECIPES = 100;
    static final int RECIPES_ITEM = 101;
    static final int FAVOURITE = 200;
    static final int FAVOURITE_ITEM = 201;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, RecipesContract.PATH_RECIPES, RECIPES);
        matcher.addURI(authority, RecipesContract.PATH_RECIPES_ITEM, RECIPES_ITEM);
        matcher.addURI(authority, RecipesContract.PATH_FAVOURITE, FAVOURITE);
        matcher.addURI(authority, RecipesContract.PATH_FAVOURITE_ITEM, FAVOURITE_ITEM);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new RecipesDbHelper(getContext());
        mFavouriteOpenHelper = new FavouriteDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case RECIPES: {
                return RecipesContract.RecipesEntry.CONTENT_TYPE;
            }
            case RECIPES_ITEM: {
                return RecipesContract.RecipesEntry.CONTENT_ITEM_TYPE;
            }
            case FAVOURITE: {
                return RecipesContract.FavouriteEntry.CONTENT_TYPE;
            }
            case FAVOURITE_ITEM: {
                return RecipesContract.FavouriteEntry.CONTENT_ITEM_TYPE;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case RECIPES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RecipesContract.RecipesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case RECIPES_ITEM: {
                SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                qb.setTables("recipes");

                qb.appendWhere("_id" + "=" + uri.getPathSegments().get(1));
                retCursor = qb.query(
                        mOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            }
            case FAVOURITE: {
                retCursor = mFavouriteOpenHelper.getReadableDatabase().query(
                        RecipesContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case FAVOURITE_ITEM: {
                SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                qb.setTables("favourite");

                qb.appendWhere("_id" + "=" + uri.getPathSegments().get(1));
                retCursor = qb.query(
                        mFavouriteOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SQLiteDatabase dbFav = mFavouriteOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case RECIPES: {
                long _id = db.insert(RecipesContract.RecipesEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = RecipesContract.RecipesEntry.buildRecipesUri(_id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVOURITE: {
                long _id = dbFav.insert(RecipesContract.FavouriteEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = RecipesContract.FavouriteEntry.buildRecipesUri(_id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SQLiteDatabase dbFav = mFavouriteOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case RECIPES: {
                rowsDeleted = db.delete(RecipesContract.RecipesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case FAVOURITE: {
                rowsDeleted = dbFav.delete(RecipesContract.FavouriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SQLiteDatabase dbFav = mFavouriteOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case RECIPES: {
                rowsUpdated = db.update(RecipesContract.RecipesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case FAVOURITE: {
                rowsUpdated = dbFav.update(RecipesContract.FavouriteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.delete(RecipesContract.RecipesEntry.TABLE_NAME, null, null);
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case RECIPES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(RecipesContract.RecipesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
