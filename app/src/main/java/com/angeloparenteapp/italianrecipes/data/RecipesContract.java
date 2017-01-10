package com.angeloparenteapp.italianrecipes.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by angel on 17/12/2016. Contract
 */

public class RecipesContract {

    public final static String CONTENT_AUTHORITY = "com.angeloparenteapp.italianrecipes";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RECIPES = "recipes";
    public static final String PATH_RECIPES_ITEM = "recipes/#";
    public static final String PATH_FAVOURITE = "favourite";
    public static final String PATH_FAVOURITE_ITEM = "favourite/#";


    public static final class RecipesEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPES;

        public static final String TABLE_NAME = "recipes";

        public static final String COLUMN_RECIPES_IMAGE = "image";

        public static final String COLUMN_RECIPES_TYPE = "type";

        public static final String COLUMN_RECIPES_INGREDIENTS = "ingredients";

        public static final String COLUMN_RECIPES_PROCESS = "process";

        public static Uri buildRecipesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildItemUri(long _id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(_id)).build();
        }
    }

    public static final class FavouriteEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;

        public static final String TABLE_NAME = "favourite";

        public static final String COLUMN_RECIPES_IMAGE = "image";

        public static final String COLUMN_RECIPES_TYPE = "type";

        public static final String COLUMN_RECIPES_INGREDIENTS = "ingredients";

        public static final String COLUMN_RECIPES_PROCESS = "process";

        public static Uri buildRecipesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildItemUri(long _id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(_id)).build();
        }
    }
}
