package com.example.BTL.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.BTL.Activities.DetailActivity;
import com.example.BTL.Domain.FilmItem;
import com.example.BTL.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {

    private static List<Integer> favoriteMovieIds;

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
        loadMovieData(filmId, holder);
    }
    private void loadMovieData(int filmId, ViewHolder holder) {
        String apiUrl = "https://movielistjava.000webhostapp.com/" + filmId;

        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, response -> {
            Gson gson = new Gson();
            FilmItem filmItem = gson.fromJson(response, FilmItem.class);

            holder.bind(filmItem);

            holder.textViewRemove.setOnClickListener(v -> {

                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    int filmIdToRemove = favoriteMovieIds.get(position);

                    SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String userId = sharedPreferences.getString("username", "");
                    DatabaseReference favoriteMoviesRef = FirebaseDatabase.getInstance().getReference()
                            .child("users").child(userId).child("favorite_movies").child(String.valueOf(filmIdToRemove));
                    favoriteMoviesRef.removeValue();

                    favoriteMovieIds.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }, error -> {
            // Xử lý lỗi khi request không thành công
        });

        Volley.newRequestQueue(holder.itemView.getContext()).add(request);
    }

    @Override
    public int getItemCount() {
        return favoriteMovieIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFilmTitle;
        ImageView imageViewFilmPoster;
        TextView textViewRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFilmPoster = itemView.findViewById(R.id.fvImg);
            textViewFilmTitle = itemView.findViewById(R.id.fvTV);
            textViewRemove = itemView.findViewById(R.id.textViewRemove); // ID của ImageView để xóa yêu thích

            imageViewFilmPoster.setOnClickListener(v -> {
                int position = getBindingAdapterPosition(); // Sử dụng getBindingAdapterPosition() thay vì getAdapterPosition()
                if (position != RecyclerView.NO_POSITION) {
                    int filmId = favoriteMovieIds.get(position);

                    // Tạo Intent để mở DetailActivity
                    Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                    intent.putExtra("id", filmId);

                    // Mở DetailActivity
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public void bind(FilmItem filmItem) {
            textViewFilmTitle.setText(filmItem.getTitle());

            Glide.with(itemView.getContext())
                    .load(filmItem.getPoster())
                    .into(imageViewFilmPoster);
        }
    }
}
