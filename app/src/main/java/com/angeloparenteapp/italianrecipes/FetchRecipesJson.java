package com.angeloparenteapp.italianrecipes;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import com.angeloparenteapp.italianrecipes.data.RecipesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * Created by angel on 20/12/2016. Fetch Json File
 */

public class FetchRecipesJson extends AsyncTask<Void, Void, Void> {

    public String name;
    public Context mContext;

    public FetchRecipesJson(Context context, String name) {
        this.mContext = context;
        this.name = name;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String json;
        try {
            InputStream inputStream = mContext.getAssets().open(name + ".json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        getJsonData(json);
        return null;
    }


    private Void getJsonData(String json) {

        try {
            JSONObject recipesJson = new JSONObject(json);
            JSONArray recipesArray = recipesJson.getJSONArray(name);
            Vector<ContentValues> cVVector = new Vector<>(recipesArray.length());

            for (int i = 0; i < recipesArray.length(); i++) {
                JSONObject jsonInside = recipesArray.getJSONObject(i);

                String type = jsonInside.getString("type");
                String process = jsonInside.getString("process");
                String image = jsonInside.getString("image");
                String ingredients = "";
                JSONArray ingredientsArray = jsonInside.getJSONArray("ingredients");
                for (int j = 0; j < ingredientsArray.length(); j++) {
                    ingredients += ingredientsArray.getString(j) + "\n";
                }

                ContentValues recipesValues = new ContentValues();

                recipesValues.put(RecipesContract.RecipesEntry.COLUMN_RECIPES_TYPE, type);
                recipesValues.put(RecipesContract.RecipesEntry.COLUMN_RECIPES_IMAGE, image);
                recipesValues.put(RecipesContract.RecipesEntry.COLUMN_RECIPES_PROCESS, process);
                recipesValues.put(RecipesContract.RecipesEntry.COLUMN_RECIPES_INGREDIENTS, ingredients);

                cVVector.add(recipesValues);
            }

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                mContext.getContentResolver().bulkInsert(RecipesContract.RecipesEntry.CONTENT_URI, cvArray);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}