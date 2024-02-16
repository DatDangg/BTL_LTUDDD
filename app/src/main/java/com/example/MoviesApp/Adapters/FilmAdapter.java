package com.example.MoviesApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MoviesApp.Activities.DetailActivity;
import com.squareup.picasso.Picasso;

import com.example.MoviesApp.Domain.Film;
import com.example.MoviesApp.R;

import java.util.ArrayList;
import java.util.Objects;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    private Context context;
    private ArrayList<Film> filmList;

    public FilmAdapter(Context context, ArrayList<Film> filmList) {
        this.context = context;
        this.filmList = filmList;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_film, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film film = filmList.get(position);
        holder.title.setText(film.getTitle());

        // Đặt OnClickListener cho ảnh
        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ID của phim
                String movieId = film.getId();
                String movieT = film.getType();
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

        // Hiển thị hình ảnh sử dụng Picasso hoặc Glide
        if (film.getPoster() != null && !film.getPoster().isEmpty()) {
            Picasso.get().load(film.getPoster()).into(holder.poster);
        }
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public static class FilmViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title, plot;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title1);
        }
    }
    public void updateList(ArrayList<Film> newList) {
        filmList.clear();
        filmList.addAll(newList);
        notifyDataSetChanged();
    }

}
