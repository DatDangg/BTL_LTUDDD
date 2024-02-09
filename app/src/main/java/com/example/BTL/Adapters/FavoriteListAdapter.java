package com.example.BTL.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BTL.Activities.DetailActivity;
import com.example.BTL.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {
    private List<String> favoriteMovieIds;

    public FavoriteListAdapter(List<String> favoriteMovieIds) {
        this.favoriteMovieIds = favoriteMovieIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(favoriteMovieIds.get(position));
    }

    @Override
    public int getItemCount() {
        return favoriteMovieIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView filmTitleTextView;
        private ImageView filmPosterImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filmTitleTextView = itemView.findViewById(R.id.fvTV);
            filmPosterImageView = itemView.findViewById(R.id.fvImg);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String filmId = favoriteMovieIds.get(position);
                    Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                    intent.putExtra("movieType", filmId.endsWith("best") ? "best" : "paid");
                    intent.putExtra("id", filmId.substring(0, filmId.length() - 4));
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public void bindData(String movieKey) {
            String firebaseUrl;
            DatabaseReference movieRef;

            firebaseUrl = "https://loginregister-34ec8-default-rtdb.firebaseio.com/";
            firebaseUrl += movieKey.endsWith("best") ? "Movies/" : movieKey.endsWith("paid") ? "PaidMovies/" : "";

            String movieId = movieKey.substring(0, movieKey.length() - 4);

            movieRef = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseUrl).child("movie" + movieId);

            movieRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String title = snapshot.child("title").getValue(String.class);
                        String poster = snapshot.child("poster").getValue(String.class);
                        filmTitleTextView.setText(title);
                        Picasso.get().load(poster).into(filmPosterImageView);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }
}
