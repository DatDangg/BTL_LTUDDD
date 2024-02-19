package com.example.BTL.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BTL.Activities.DetailActivity;
import com.example.BTL.Domain.Film;
import com.example.BTL.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class AllMoviesAdapter extends RecyclerView.Adapter<AllMoviesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Film> movieList;

    public AllMoviesAdapter(Context context, ArrayList<Film> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Film movie = movieList.get(position);
        holder.movieTitle.setText(movie.getTitle());
        // Load ảnh từ URL bằng Picasso hoặc thư viện hỗ trợ tương tự
        Picasso.get().load(movie.getPoster()).into(holder.moviePoster);

        holder.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ID của phim
                String movieId = movie.getId();
                String movieT = movie.getType();
                // Tạo Intent để chuyển sang DetailActivity và truyền ID của phim
                Intent intent = new Intent(context, DetailActivity.class);
                if (Objects.equals(movieT, "best")) {
                    intent.putExtra("movieType", "best");
                } else {
                    intent.putExtra("movieType", "paid");
                }
                intent.putExtra("id", movieId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);

        }
    }
}
