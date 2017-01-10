package com.angeloparenteapp.italianrecipes.favourite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.angeloparenteapp.italianrecipes.MapsActivity;
import com.angeloparenteapp.italianrecipes.R;
import com.angeloparenteapp.italianrecipes.data.FavouriteDbHelper;
import com.angeloparenteapp.italianrecipes.data.RecipesContract;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavoriteDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mUri;
    TextView detailTitle;
    TextView detailIngredients;
    TextView detailProcess;
    ImageView detailImage;
    FloatingActionButton fab;
    Button mapButton;

    int pref;
    int notPref;

    FavouriteDbHelper favHelper;
    SQLiteDatabase db;

    private static final String[] RECIPES_COLUMNS_FAV_DETAIL = {
            RecipesContract.FavouriteEntry.TABLE_NAME + "." + RecipesContract.FavouriteEntry._ID,
            RecipesContract.FavouriteEntry.COLUMN_RECIPES_TYPE,
            RecipesContract.FavouriteEntry.COLUMN_RECIPES_IMAGE,
            RecipesContract.FavouriteEntry.COLUMN_RECIPES_INGREDIENTS,
            RecipesContract.FavouriteEntry.COLUMN_RECIPES_PROCESS
    };

    static final int COL_RECIPES_ID_FAV = 0;
    static final int COL_RECIPES_TYPE_FAV = 1;
    static final int COL_RECIPES_IMAGE_FAV = 2;
    static final int COL_RECIPES_INGREDIENTS_FAV = 3;
    static final int COL_RECIPES_PROCESS_FAV = 4;

    public FavoriteDetailFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_detail, container, false);

        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");
        AdView mAdView = (AdView) view.findViewById(R.id.adViewFav);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        pref = R.drawable.ic_heart_white_18dp;
        notPref = R.drawable.ic_heart_outline_white_18dp;

        Intent intent = getActivity().getIntent();
        mUri = intent.getData();

        favHelper = new FavouriteDbHelper(getContext());
        db = favHelper.getReadableDatabase();

        detailImage = (ImageView) view.findViewById(R.id.detailImageFav);
        detailTitle = (TextView) view.findViewById(R.id.detailTitleFav);
        detailIngredients = (TextView) view.findViewById(R.id.detailIngredientsFav);
        detailProcess = (TextView) view.findViewById(R.id.detailProcessFav);
        mapButton = (Button) view.findViewById(R.id.buttonFav);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("lat", 37.42);
                intent.putExtra("lon", 14.65);
                startActivity(intent);
            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.fabFav);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        if (null != mUri) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(getActivity(),
                    mUri,
                    RECIPES_COLUMNS_FAV_DETAIL,
                    null,
                    null,
                    null);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        if (data != null && data.moveToFirst()) {
            String image = data.getString(COL_RECIPES_IMAGE_FAV);
            if (image.equals("Pasta Al Pomodoro")) {
                Glide.with(getContext()).load(R.drawable.pasta_al_pomodoro).into(detailImage);
            } else if (image.equals("Pasta In Bianco")) {
                Glide.with(getContext()).load(R.drawable.pasta_in_bianco).into(detailImage);
            } else if (image.equals("margherita")) {
                Glide.with(getContext()).load(R.drawable.margherita).into(detailImage);
            } else if (image.equals("diavola")) {
                Glide.with(getContext()).load(R.drawable.diavola).into(detailImage);
            } else if (image.equals("tagliata")) {
                Glide.with(getContext()).load(R.drawable.tagliata).into(detailImage);
            } else if (image.equals("pollo")) {
                Glide.with(getContext()).load(R.drawable.pollo).into(detailImage);
            }

            final String title = data.getString(COL_RECIPES_TYPE_FAV);
            detailTitle.setText(title);

            if (!isEmpty()) {
                if (!isFavorite(title)) {
                    fab.setImageResource(notPref);
                } else {
                    fab.setImageResource(pref);
                }
            } else {
                fab.setImageResource(notPref);
            }

            String ingredients = data.getString(COL_RECIPES_INGREDIENTS_FAV);
            detailIngredients.setText(ingredients);

            String process = data.getString(COL_RECIPES_PROCESS_FAV);
            detailProcess.setText(process);

            final ContentValues values = new ContentValues();
            values.put(RecipesContract.FavouriteEntry.COLUMN_RECIPES_TYPE, title);
            values.put(RecipesContract.FavouriteEntry.COLUMN_RECIPES_IMAGE, image);
            values.put(RecipesContract.FavouriteEntry.COLUMN_RECIPES_INGREDIENTS, ingredients);
            values.put(RecipesContract.FavouriteEntry.COLUMN_RECIPES_PROCESS, process);


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isEmpty()) {
                        if (!isFavorite(title)) {
                            getContext().getContentResolver().insert(RecipesContract.FavouriteEntry.CONTENT_URI, values);
                            fab.setImageResource(pref);
                            Toast.makeText(getContext(), "Add to favourites", Toast.LENGTH_SHORT).show();
                        } else {
                            SQLiteDatabase db = new FavouriteDbHelper(getContext()).getReadableDatabase();
                            db.delete(RecipesContract.FavouriteEntry.TABLE_NAME, RecipesContract.FavouriteEntry.COLUMN_RECIPES_TYPE + " =\"" + title + "\";", null);
                            fab.setImageResource(notPref);
                            Toast.makeText(getContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        fab.setImageResource(pref);
                        getContext().getContentResolver().insert(RecipesContract.FavouriteEntry.CONTENT_URI, values);
                        Toast.makeText(getContext(), "Add to favourites", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().restartLoader(0, null, this);
    }

    public boolean isFavorite(String name) {
        String query = "Select * from " + RecipesContract.FavouriteEntry.TABLE_NAME
                + " where " + RecipesContract.FavouriteEntry.COLUMN_RECIPES_TYPE
                + " =\"" + name + "\";";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean isEmpty() {
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
