package com.draker.recmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.draker.recmaster.R;
import com.draker.recmaster.database.entity.MovieEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Адаптер для отображения истории просмотра фильмов
 */
public class WatchHistoryAdapter extends RecyclerView.Adapter<WatchHistoryAdapter.WatchHistoryViewHolder> {
    
    private final Context context;
    private final List<MovieEntity> watchedMovies;
    private Map<Integer, String> genreMap;
    private final OnItemClickListener listener;
    
    // Интерфейс для обработки кликов
    public interface OnItemClickListener {
        void onItemClick(MovieEntity movie);
    }
    
    public WatchHistoryAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.watchedMovies = new ArrayList<>();
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public WatchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_watched_movie, parent, false);
        return new WatchHistoryViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull WatchHistoryViewHolder holder, int position) {
        MovieEntity movie = watchedMovies.get(position);
        holder.bind(movie, listener);
    }
    
    @Override
    public int getItemCount() {
        return watchedMovies.size();
    }
    
    /**
     * Обновление списка просмотренных фильмов
     */
    public void setWatchedMovies(List<MovieEntity> movies) {
        this.watchedMovies.clear();
        if (movies != null) {
            this.watchedMovies.addAll(movies);
        }
        notifyDataSetChanged();
    }
    
    /**
     * Установка карты жанров для отображения названий жанров
     */
    public void setGenreMap(Map<Integer, String> genreMap) {
        this.genreMap = genreMap;
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder для элемента истории просмотра
     */
    class WatchHistoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView posterImageView;
        private final TextView titleTextView;
        private final TextView genresTextView;
        private final TextView watchedDateTextView;
        private final RatingBar userRatingBar;
        private final TextView userNotesTextView;
        
        public WatchHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.ivMoviePoster);
            titleTextView = itemView.findViewById(R.id.tvMovieTitle);
            genresTextView = itemView.findViewById(R.id.tvMovieGenres);
            watchedDateTextView = itemView.findViewById(R.id.tvWatchedDate);
            userRatingBar = itemView.findViewById(R.id.rbUserRating);
            userNotesTextView = itemView.findViewById(R.id.tvUserNotes);
        }
        
        public void bind(final MovieEntity movie, final OnItemClickListener listener) {
            titleTextView.setText(movie.getTitle());
            
            // Загрузка постера
            if (movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) {
                String posterUrl = "https://image.tmdb.org/t/p/w342" + movie.getPosterPath();
                Glide.with(context)
                        .load(posterUrl)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.placeholder_poster)
                                .error(R.drawable.error_poster))
                        .into(posterImageView);
            } else {
                posterImageView.setImageResource(R.drawable.placeholder_poster);
            }
            
            // Отображение жанров
            if (genreMap != null && movie.getGenreIds() != null && !movie.getGenreIds().isEmpty()) {
                StringBuilder genresBuilder = new StringBuilder();
                for (Integer genreId : movie.getGenreIds()) {
                    String genreName = genreMap.get(genreId);
                    if (genreName != null) {
                        if (genresBuilder.length() > 0) {
                            genresBuilder.append(", ");
                        }
                        genresBuilder.append(genreName);
                    }
                }
                genresTextView.setText(genresBuilder.toString());
            } else {
                genresTextView.setText(R.string.unknown_genre);
            }
            
            // Отображение даты просмотра
            if (movie.getWatchedDate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
                watchedDateTextView.setText(context.getString(R.string.watched_on, dateFormat.format(movie.getWatchedDate())));
            } else {
                watchedDateTextView.setText(R.string.watched);
            }
            
            // Отображение пользовательского рейтинга
            userRatingBar.setRating(movie.getUserRating());
            
            // Отображение заметок пользователя
            if (movie.getUserNotes() != null && !movie.getUserNotes().isEmpty()) {
                userNotesTextView.setVisibility(View.VISIBLE);
                userNotesTextView.setText(movie.getUserNotes());
            } else {
                userNotesTextView.setVisibility(View.GONE);
            }
            
            // Обработка клика по элементу
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(watchedMovies.get(position));
                    }
                }
            });
        }
    }
}