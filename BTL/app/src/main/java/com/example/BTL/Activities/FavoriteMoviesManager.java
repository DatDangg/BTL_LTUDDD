package com.example.BTL.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMoviesManager extends AppCompatActivity {

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

}