package com.draker.recmaster.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.draker.recmaster.R;
import com.draker.recmaster.model.Movie;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Адаптер для отображения списка фильмов
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String TAG = "MovieAdapter";

    private List<Movie> movies;
    private final Context context;
    private final OnMovieClickListener listener;
    private Map<Integer, String> genreMap;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(Context context, OnMovieClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie, listener);
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        Log.d(TAG, "Setting movies: " + (movies != null ? movies.size() : 0));
        notifyDataSetChanged();
    }

    public void setGenreMap(Map<Integer, String> genreMap) {
        this.genreMap = genreMap;
        Log.d(TAG, "Setting genre map: " + (genreMap != null ? genreMap.size() : 0));
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageMoviePoster;
        private final TextView textMovieTitle;
        private final TextView textMovieRating;
        private final TextView textMovieReleaseDate;
        private final ChipGroup chipGroupGenres;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMoviePoster = itemView.findViewById(R.id.image_movie_poster);
            textMovieTitle = itemView.findViewById(R.id.text_movie_title);
            textMovieRating = itemView.findViewById(R.id.text_movie_rating);
            textMovieReleaseDate = itemView.findViewById(R.id.text_movie_release_date);
            chipGroupGenres = itemView.findViewById(R.id.chip_group_genres);
        }

        public void bind(Movie movie, OnMovieClickListener listener) {
            Log.d(TAG, "Binding movie: " + movie.getTitle());
            
            textMovieTitle.setText(movie.getTitle());
            textMovieRating.setText(String.format("%.1f ★", movie.getVoteAverage()));
            
            // Используем форматированную дату
            String formattedDate = movie.getFormattedReleaseDate();
            textMovieReleaseDate.setText(formattedDate.isEmpty() ? "Дата не указана" : formattedDate);

            // Загрузка изображения с помощью Glide
            if (movie.getPosterUrl() != null) {
                Log.d(TAG, "Loading poster: " + movie.getPosterUrl());
                
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.ic_movie_placeholder)
                        .error(R.drawable.ic_movie_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                
                Glide.with(context)
                        .load(movie.getPosterUrl())
                        .apply(requestOptions)
                        .into(imageMoviePoster);
            } else {
                Log.d(TAG, "No poster URL, using placeholder");
                imageMoviePoster.setImageResource(R.drawable.ic_movie_placeholder);
            }

            // Добавление жанров в виде чипов
            chipGroupGenres.removeAllViews();
            if (genreMap != null && movie.getGenreIds() != null) {
                for (Integer genreId : movie.getGenreIds()) {
                    String genreName = genreMap.get(genreId);
                    if (genreName != null) {
                        Chip chip = new Chip(context);
                        chip.setText(genreName);
                        chip.setClickable(false);
                        chip.setCheckable(false);
                        chipGroupGenres.addView(chip);

                        // Ограничение количества чипов для экономии места
                        if (chipGroupGenres.getChildCount() >= 3) {
                            break;
                        }
                    }
                }
            }

            // Обработка нажатия на элемент
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMovieClick(movie);
                }
            });
        }
    }
}