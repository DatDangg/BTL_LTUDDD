package com.example.BTL.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.BTL.Domain.FilmItem;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMoviesManager extends AppCompatActivity {

    private static FilmItem[] filmItems;

    public FavoriteMoviesManager(List<Integer> favoriteMovieIds) {
        FavoriteMoviesManager.favoriteMovieIds = favoriteMovieIds;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private static List<Integer> favoriteMovieIds = new ArrayList<>();

    public static void addFavoriteMovie(int movieId) {
        if (!favoriteMovieIds.contains(movieId)) {
            favoriteMovieIds.add(movieId);
        }
    }

    public static void removeFavoriteMovie(int movieId) {
        favoriteMovieIds.remove(Integer.valueOf(movieId));
    }

    public static List<Integer> getFavoriteMovies() {
        return favoriteMovieIds;
    }

    public static FilmItem getFilmItemById(int movieId) {
        // Lặp qua danh sách yêu thích và trả về FilmItem có id tương ứng
        for (FilmItem item : filmItems) {
            if (item.getId() == movieId) {
                return item;
            }
        }
        return null;
    }
}