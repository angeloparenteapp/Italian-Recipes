package com.angeloparenteapp.italianrecipes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.angeloparenteapp.italianrecipes.favourite.FavouriteList;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by angel on 16/11/2016. Main Activity Adapter
 */

public class RecipesCardAdapter extends RecyclerView.Adapter<RecipesCardAdapter.MyViewHolder> {

    private Context mContext;
    private List<RecipesCard> recipesCardList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;
        ImageView imageView;
        CardView mView;

        public MyViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            mView = (CardView) view.findViewById(R.id.card_view);
            title = (TextView) view.findViewById(R.id.card_title);
            description = (TextView) view.findViewById(R.id.card_description);
            imageView = (ImageView) view.findViewById(R.id.card_image);
        }
    }

    public RecipesCardAdapter(Context context, List<RecipesCard> recipesCardList) {
        this.mContext = context;
        this.recipesCardList = recipesCardList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final RecipesCard recipesCard = recipesCardList.get(position);

        if (recipesCard.getTitle().equals("pasta")) {
            holder.title.setText(R.string.pasta);
        } else if (recipesCard.getTitle().equals("pizza")) {
            holder.title.setText(R.string.pizza);
        } else if (recipesCard.getTitle().equals("main")) {
            holder.title.setText(R.string.main);
        } else if (recipesCard.getTitle().equals("favourite")) {
            holder.title.setText(R.string.favourite);
        }

        holder.description.setText(recipesCard.getDescription());
        Glide.with(mContext).load(recipesCard.getImage()).into(holder.imageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recipesCard.getTitle().equals("favourite")){
                    Intent intent = new Intent(mContext, FavouriteList.class);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, RecipesList.class);
                    intent.putExtra("name", recipesCard.getTitle());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipesCardList.size();
    }
}
