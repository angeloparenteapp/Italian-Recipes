package com.angeloparenteapp.italianrecipes;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecipesCardAdapter recipesCardAdapter;
    List<RecipesCard> recipesCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.cardList);

        recipesCardList = new ArrayList<>();
        prepareCardAlbum();

        recipesCardAdapter = new RecipesCardAdapter(this, recipesCardList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipesCardAdapter);
    }

    public void prepareCardAlbum() {

        int[] covers = new int[]{
                R.drawable.pasta,
                R.drawable.pizza,
                R.drawable.carne,
                R.drawable.favourite};

        RecipesCard pasta = new RecipesCard("pasta", "Try the best italian pasta recipes", covers[0]);
        recipesCardList.add(pasta);

        RecipesCard pizza = new RecipesCard("pizza", "Try the best italian pizza recipes", covers[1]);
        recipesCardList.add(pizza);

        RecipesCard main = new RecipesCard("main", "Try the best italian main course recipes", covers[2]);
        recipesCardList.add(main);

        RecipesCard favourite = new RecipesCard("favourite", "Your favourite recipes", covers[3]);
        recipesCardList.add(favourite);
    }
}
