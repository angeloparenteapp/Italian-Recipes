package com.angeloparenteapp.italianrecipes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by angel on 22/12/2016. Recipes List Adapter
 */

public class RecipesListAdapter extends CursorAdapter {

    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView titleView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.gridImage);
            titleView = (TextView) view.findViewById(R.id.gridText);
        }
    }

    public RecipesListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_layout_elements, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();

            String title = cursor.getString(RecipesListFragment.COL_RECIPES_TYPE);
            viewHolder.titleView.setText(title);

            String image = cursor.getString(RecipesListFragment.COL_RECIPES_IMAGE);
            if (image.equals("Pasta Al Pomodoro")) {
                Glide.with(context).load(R.drawable.pasta_al_pomodoro).into(viewHolder.iconView);
            } else if (image.equals("Pasta In Bianco")) {
                Glide.with(context).load(R.drawable.pasta_in_bianco).into(viewHolder.iconView);
            } else if (image.equals("margherita")) {
                Glide.with(context).load(R.drawable.margherita).into(viewHolder.iconView);
            } else if (image.equals("diavola")) {
                Glide.with(context).load(R.drawable.diavola).into(viewHolder.iconView);
            } else if (image.equals("tagliata")) {
                Glide.with(context).load(R.drawable.tagliata).into(viewHolder.iconView);
            } else if (image.equals("pollo")) {
                Glide.with(context).load(R.drawable.pollo).into(viewHolder.iconView);
            }
        }
    }
}
