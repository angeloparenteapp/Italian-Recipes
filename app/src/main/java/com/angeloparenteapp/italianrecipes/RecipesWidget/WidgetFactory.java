package com.angeloparenteapp.italianrecipes.RecipesWidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.angeloparenteapp.italianrecipes.R;
import com.angeloparenteapp.italianrecipes.data.RecipesContract;

/**
 * Created by angel on 31/12/2016.
 */

public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor mCursor;
    private Context mContext;
    int mWidgetId;


    public WidgetFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
        this.mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.grid_layout_elements);

        if (mCursor.moveToPosition(position)) {
            String title = mCursor.getString(1);

            remoteViews.setTextViewText(R.id.gridText, title);

            remoteViews.setImageViewResource(R.id.gridImage, R.mipmap.ic_launcher);
        }

        return remoteViews;
    }

    @Override
    public void onCreate() {
        mCursor = mContext.getContentResolver().query(RecipesContract.FavouriteEntry.CONTENT_URI,
                new String[]{RecipesContract.FavouriteEntry._ID,
                        RecipesContract.FavouriteEntry.COLUMN_RECIPES_TYPE},
                null,
                null,
                null);
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = mContext.getContentResolver().query(RecipesContract.FavouriteEntry.CONTENT_URI,
                new String[]{RecipesContract.FavouriteEntry._ID,
                        RecipesContract.FavouriteEntry.COLUMN_RECIPES_TYPE},
                null,
                null,
                null);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
