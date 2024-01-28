package com.example.BTL.Adapters;

import android.content.Intent;
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
import com.example.BTL.Activities.FavoriteMoviesManager;
import com.example.BTL.Domain.FilmItem;
import com.example.BTL.R;
//import com.example.BTL.Utils.NetworkUtils;
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
        // Thực hiện request để lấy thông tin phim
        String apiUrl = "https://moviesapi.ir/api/v1/movies/" + filmId;
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, response -> {
            // Parse dữ liệu từ JSON response
            Gson gson = new Gson();
            FilmItem filmItem = gson.fromJson(response, FilmItem.class);

            // Hiển thị dữ liệu lên ViewHolder
            holder.bind(filmItem);
        }, error -> {
            // Xử lý lỗi khi request không thành công
        });

        // Thêm request vào hàng đợi của Volley
        Volley.newRequestQueue(holder.itemView.getContext()).add(request);
    }

    @Override
    public int getItemCount() {
        return favoriteMovieIds.size();
    }

    // Trong ViewHolder
    // Trong ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFilmTitle;
        ImageView imageViewFilmPoster;
        TextView textViewRemove; // Thêm ImageView để xóa yêu thích

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFilmPoster = itemView.findViewById(R.id.fvImg);
            textViewFilmTitle = itemView.findViewById(R.id.fvTV);
            textViewRemove = itemView.findViewById(R.id.textViewRemove); // ID của ImageView để xóa yêu thích

            // Thêm listener cho imageViewRemove
            textViewRemove.setOnClickListener(v -> {
                int position = getBindingAdapterPosition(); // Sử dụng getBindingAdapterPosition() thay vì getAdapterPosition()
                if (position != RecyclerView.NO_POSITION) {
                    int filmId = favoriteMovieIds.get(position);

                    // Gọi phương thức removeFavoriteMovie để xóa phim khỏi danh sách yêu thích
                    FavoriteMoviesManager.removeFavoriteMovie(filmId);

                    // Cập nhật lại danh sách yêu thích và thông báo cho Adapter cập nhật giao diện
                    favoriteMovieIds = FavoriteMoviesManager.getFavoriteMovies();
                    notifyDataSetChanged(); // Gọi phương thức này để cập nhật giao diện
                }
            });

// Thêm listener cho imageViewFilmPoster
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
            // Hiển thị thông tin phim lên ViewHolder
            textViewFilmTitle.setText(filmItem.getTitle());

            // Sử dụng Glide để hiển thị hình ảnh từ URL
            Glide.with(itemView.getContext())
                    .load(filmItem.getPoster())
                    .into(imageViewFilmPoster);
        }
    }


}
