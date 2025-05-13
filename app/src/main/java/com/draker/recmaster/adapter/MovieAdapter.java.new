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
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestOptions;
import com.draker.recmaster.R;
import com.draker.recmaster.model.Movie;
import com.draker.recmaster.util.MovieWatchUtil;
import com.draker.recmaster.viewmodel.WatchHistoryViewModel;
import com.google.android.material.button.MaterialButton;
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
    private WatchHistoryViewModel watchHistoryViewModel;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public interface OnMarkWatchedClickListener {
        void onMarkWatchedClick(Movie movie);
    }

    private OnMarkWatchedClickListener markWatchedListener;

    public MovieAdapter(Context context, OnMovieClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.movies = new ArrayList<>();
    }

    public void setWatchHistoryViewModel(WatchHistoryViewModel viewModel) {
        this.watchHistoryViewModel = viewModel;
    }

    public void setOnMarkWatchedClickListener(OnMarkWatchedClickListener listener) {
        this.markWatchedListener = listener;
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
        holder.bind(movie, listener, markWatchedListener);
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
        private final MaterialButton btnMarkWatched;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMoviePoster = itemView.findViewById(R.id.image_movie_poster);
            textMovieTitle = itemView.findViewById(R.id.text_movie_title);
            textMovieRating = itemView.findViewById(R.id.text_movie_rating);
            textMovieReleaseDate = itemView.findViewById(R.id.text_movie_release_date);
            chipGroupGenres = itemView.findViewById(R.id.chip_group_genres);
            btnMarkWatched = itemView.findViewById(R.id.btn_mark_watched);
        }

        public void bind(Movie movie, OnMovieClickListener listener, OnMarkWatchedClickListener markWatchedListener) {
            Log.d(TAG, "Binding movie: " + movie.getTitle());
            
            textMovieTitle.setText(movie.getTitle());
            
            // Проверка и установка оценки
            float rating = movie.getVoteAverage();
            Log.d(TAG, "Movie rating: " + rating + " for " + movie.getTitle());
            textMovieRating.setText(String.format("%.1f ★", rating));
            
            // Используем форматированную дату
            String formattedDate = movie.getFormattedReleaseDate();
            Log.d(TAG, "Movie release date: " + formattedDate + " for " + movie.getTitle());
            textMovieReleaseDate.setText(formattedDate.isEmpty() ? "Дата не указана" : formattedDate);

            // Загрузка изображения с помощью Glide
            String posterUrl = movie.getPosterUrl();
            if (posterUrl != null && !posterUrl.isEmpty()) {
                Log.d(TAG, "Loading poster: " + posterUrl + " for " + movie.getTitle());
                
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.ic_movie_placeholder)
                        .error(R.drawable.ic_movie_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                
                Glide.with(context)
                        .load(posterUrl)
                        .apply(requestOptions)
                        .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                                Log.e(TAG, "Failed to load image: " + posterUrl + " for " + movie.getTitle(), e);
                                return false; // позволяем Glide обработать ошибку и отобразить placeholder
                            }

                            @Override
                            public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                Log.d(TAG, "Successfully loaded image: " + posterUrl + " for " + movie.getTitle());
                                return false; // позволяем Glide отобразить загруженное изображение
                            }
                        })
                        .into(imageMoviePoster);
            } else {
                Log.d(TAG, "No poster URL, using placeholder for " + movie.getTitle());
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

            // Установка обработчика для кнопки "Отметить как просмотренное"
            btnMarkWatched.setOnClickListener(v -> {
                if (watchHistoryViewModel != null) {
                    // Используем утилитный класс для показа диалога
                    MovieWatchUtil.showMarkAsWatchedDialog(context, movie, watchHistoryViewModel);
                } else if (markWatchedListener != null) {
                    markWatchedListener.onMarkWatchedClick(movie);
                }
            });

            // Обработка нажатия на элемент
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMovieClick(movie);
                }
            });
        }
    }
}