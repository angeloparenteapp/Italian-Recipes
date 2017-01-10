package com.angeloparenteapp.italianrecipes.favourite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.angeloparenteapp.italianrecipes.R;
import com.bumptech.glide.Glide;

/**
 * Created by angel on 28/12/2016.
 */

public class FavouriteListAdapter extends CursorAdapter {

    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView titleView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.gridFavouriteImage);
            titleView = (TextView) view.findViewById(R.id.gridFavouriteText);
        }
    }

    public FavouriteListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_grid_layout_elements, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();

            String title = cursor.getString(FavouriteListFragment.COL_RECIPES_TYPE_FAV);
            viewHolder.titleView.setText(title);
            /*if (title.equals("PastaAlPomodoro")) {
                viewHolder.titleView.setText(R.string.pasta_al_pomodoro);
            } else if (title.equals("PastaInBianco")) {
                viewHolder.titleView.setText(R.string.pasta_in_bianco);
            } else if (title.equals("margherita")) {
                viewHolder.titleView.setText(title);
            } else if (title.equals("diavola")) {
                viewHolder.titleView.setText(title);
            } else if (title.equals("tagliata")) {
                viewHolder.titleView.setText(title);
            } else if (title.equals("pollo")) {
                viewHolder.titleView.setText(R.string.pollo_al_forno);
            }*/

            String image = cursor.getString(FavouriteListFragment.COL_RECIPES_IMAGE_FAV);
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
