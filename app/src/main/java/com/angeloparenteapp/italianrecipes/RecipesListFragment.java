package com.angeloparenteapp.italianrecipes;

import android.content.Intent;
import android.database.Cursor;
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

import com.angeloparenteapp.italianrecipes.data.RecipesContract;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecipesListAdapter mRecipesListAdapter;
    private GridView mGridView;

    private static final String[] RECIPES_COLUMNS = {
            RecipesContract.RecipesEntry.TABLE_NAME + "." + RecipesContract.RecipesEntry._ID,
            RecipesContract.RecipesEntry.COLUMN_RECIPES_TYPE,
            RecipesContract.RecipesEntry.COLUMN_RECIPES_IMAGE
    };

    static final int COL_RECIPES_ID = 0;
    static final int COL_RECIPES_TYPE = 1;
    static final int COL_RECIPES_IMAGE = 2;

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    public RecipesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);

        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");
        AdView mAdView = (AdView) view.findViewById(R.id.adViewList);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getActivity().getIntent();
        String name = intent.getStringExtra("name");

        FetchRecipesJson fetchRecipesJson = new FetchRecipesJson(getContext(), name);
        fetchRecipesJson.execute();

        mRecipesListAdapter = new RecipesListAdapter(getActivity(), null, 0);

        mGridView = (GridView) view.findViewById(R.id.gridView);
        mGridView.setAdapter(mRecipesListAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                ((Callback) getActivity())
                        .onItemSelected(RecipesContract.RecipesEntry.buildItemUri(cursor.getLong(COL_RECIPES_ID)));
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri recipesUri = RecipesContract.RecipesEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                recipesUri,
                RECIPES_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRecipesListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecipesListAdapter.swapCursor(null);
    }
}
