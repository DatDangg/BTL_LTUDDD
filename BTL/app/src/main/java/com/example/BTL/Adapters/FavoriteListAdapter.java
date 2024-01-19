package com.example.BTL.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BTL.R;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {

    private List<Integer> favoriteMovieIds;

    public FavoriteListAdapter(List<Integer> favoriteItemIds) {
        this.favoriteMovieIds = favoriteItemIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int filmId = favoriteMovieIds.get(position);
    }

    @Override
    public int getItemCount() {
        return favoriteMovieIds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFilmTitle;
        ImageView imageViewFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFavorite = itemView.findViewById(R.id.fvImg);
            textViewFilmTitle = itemView.findViewById(R.id.fvTV);
        }
    }
}
