package com.angeloparenteapp.italianrecipes.favourite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.angeloparenteapp.italianrecipes.R;
import com.angeloparenteapp.italianrecipes.RecipesListFragment;
import com.angeloparenteapp.italianrecipes.data.FavouriteDbHelper;
import com.angeloparenteapp.italianrecipes.data.RecipesContract;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavouriteListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private FavouriteListAdapter mFavListAdapter;
    private GridView mFavGridView;

    private static final String[] RECIPES_COLUMNS_FAVOURITE = {
            RecipesContract.FavouriteEntry.TABLE_NAME + "." + RecipesContract.FavouriteEntry._ID,
            RecipesContract.FavouriteEntry.COLUMN_RECIPES_TYPE,
            RecipesContract.FavouriteEntry.COLUMN_RECIPES_IMAGE
    };

    static final int COL_RECIPES_ID_FAV = 0;
    static final int COL_RECIPES_TYPE_FAV = 1;
    static final int COL_RECIPES_IMAGE_FAV = 2;

    public FavouriteListFragment() {
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_list, container, false);

        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");
        AdView mAdView = (AdView) view.findViewById(R.id.adViewFavouriteList);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mFavListAdapter = new FavouriteListAdapter(getActivity(), null, 0);

        mFavGridView = (GridView) view.findViewById(R.id.gridViewFavourite);
        mFavGridView.setAdapter(mFavListAdapter);

        mFavGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                ((Callback) getActivity())
                        .onItemSelected(RecipesContract.FavouriteEntry.buildItemUri(cursor.getLong(COL_RECIPES_ID_FAV)));
            }
        });

        if (isEmpty(getContext())) {
            Toast.makeText(getContext(), "No Favourites", Toast.LENGTH_SHORT).show();
        }

        mFavGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                getContext().getContentResolver().delete(
                        RecipesContract.FavouriteEntry.CONTENT_URI, null, null);
                return true;
            }
        });

        mFavListAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri recipesUri = RecipesContract.FavouriteEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                recipesUri,
                RECIPES_COLUMNS_FAVOURITE,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFavListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().restartLoader(0, null, this);
        mFavListAdapter.swapCursor(null);
    }

    public boolean isEmpty(Context context) {
        FavouriteDbHelper favHelper;
        SQLiteDatabase db;
        favHelper = new FavouriteDbHelper(context);
        db = favHelper.getReadableDatabase();
        String count = "SELECT count(*) FROM favourite";
        Cursor mCursor = db.rawQuery(count, null);
        mCursor.moveToFirst();
        int iCount = mCursor.getInt(0);
        if (iCount > 0) {
            mCursor.close();
            return false;
        } else {
            mCursor.close();
            return true;
        }
    }
}
