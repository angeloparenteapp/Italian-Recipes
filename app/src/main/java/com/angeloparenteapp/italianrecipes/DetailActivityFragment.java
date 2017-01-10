package com.angeloparenteapp.italianrecipes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
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

import com.angeloparenteapp.italianrecipes.data.FavouriteDbHelper;
import com.angeloparenteapp.italianrecipes.data.RecipesContract;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

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

    private static final String[] RECIPES_COLUMNS_DETAIL = {
            RecipesContract.RecipesEntry.TABLE_NAME + "." + RecipesContract.RecipesEntry._ID,
            RecipesContract.RecipesEntry.COLUMN_RECIPES_TYPE,
            RecipesContract.RecipesEntry.COLUMN_RECIPES_IMAGE,
            RecipesContract.RecipesEntry.COLUMN_RECIPES_INGREDIENTS,
            RecipesContract.RecipesEntry.COLUMN_RECIPES_PROCESS
    };

    static final int COL_RECIPES_ID = 0;
    static final int COL_RECIPES_TYPE = 1;
    static final int COL_RECIPES_IMAGE = 2;
    static final int COL_RECIPES_INGREDIENTS = 3;
    static final int COL_RECIPES_PROCESS = 4;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        pref = R.drawable.ic_heart_white_18dp;
        notPref = R.drawable.ic_heart_outline_white_18dp;

        Intent intent = getActivity().getIntent();
        mUri = intent.getData();

        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");
        AdView mAdView = (AdView) view.findViewById(R.id.adViewDetail);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        detailImage = (ImageView) view.findViewById(R.id.detailImage);
        detailTitle = (TextView) view.findViewById(R.id.detailTitle);
        detailIngredients = (TextView) view.findViewById(R.id.detailIngredients);
        detailProcess = (TextView) view.findViewById(R.id.detailProcess);
        mapButton = (Button) view.findViewById(R.id.button);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("lat", 37.42);
                intent.putExtra("lon", 14.65);
                startActivity(intent);
            }
        });

        favHelper = new FavouriteDbHelper(getContext());
        db = favHelper.getReadableDatabase();


        fab = (FloatingActionButton) view.findViewById(R.id.fab);

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
                    RECIPES_COLUMNS_DETAIL,
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
            String image = data.getString(COL_RECIPES_IMAGE);
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

            final String title = data.getString(COL_RECIPES_TYPE);
            detailTitle.setText(title);
            /*if (title.equals("PastaAlPomodoro")) {
                detailTitle.setText(R.string.pasta_al_pomodoro);
            } else if (title.equals("PastaInBianco")) {
                detailTitle.setText(R.string.pasta_in_bianco);
            } else if (title.equals("margherita")) {
                detailTitle.setText(title);
            } else if (title.equals("diavola")) {
                detailTitle.setText(title);
            } else if (title.equals("tagliata")) {
                detailTitle.setText(title);
            } else if (title.equals("pollo")) {
                detailTitle.setText(R.string.pollo_al_forno);
            }*/

            if (!isEmpty()) {
                if (!isFavorite(title)) {
                    fab.setImageResource(notPref);
                } else {
                    fab.setImageResource(pref);
                }
            } else {
                fab.setImageResource(notPref);
            }

            String ingredients = data.getString(COL_RECIPES_INGREDIENTS);
            detailIngredients.setText(ingredients);

            String process = data.getString(COL_RECIPES_PROCESS);
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