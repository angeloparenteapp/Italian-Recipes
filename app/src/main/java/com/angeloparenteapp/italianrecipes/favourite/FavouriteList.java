package com.angeloparenteapp.italianrecipes.favourite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.angeloparenteapp.italianrecipes.DetailActivity;
import com.angeloparenteapp.italianrecipes.R;

public class FavouriteList extends AppCompatActivity implements FavouriteListFragment.Callback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        Intent intent = new Intent(this, FavoriteDetail.class).setData(contentUri);
        startActivity(intent);
    }
}
