package com.draker.recmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.draker.recmaster.R;
import com.draker.recmaster.model.TvShow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Адаптер для отображения списка сериалов в RecyclerView
 */
public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {

    private final Context context;
    private final List<TvShow> tvShows;
    private final OnTvShowClickListener listener;
    private Map<Integer, String> genreMap;

    /**
     * Интерфейс для обработки событий клика на сериал
     */
    public interface OnTvShowClickListener {
        void onTvShowClick(TvShow tvShow);
    }

    /**
     * Конструктор адаптера
     * @param context контекст активности
     * @param listener слушатель кликов
     */
    public TvShowAdapter(Context context, OnTvShowClickListener listener) {
        this.context = context;
        this.tvShows = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tv_show, parent, false);
        return new TvShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder holder, int position) {
        TvShow tvShow = tvShows.get(position);
        holder.bind(tvShow, listener);
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    /**
     * Обновляет список сериалов
     * @param newTvShows новый список сериалов
     */
    public void setTvShows(List<TvShow> newTvShows) {
        this.tvShows.clear();
        if (newTvShows != null) {
            this.tvShows.addAll(newTvShows);
        }
        notifyDataSetChanged();
    }

    /**
     * Устанавливает карту жанров
     * @param genreMap карта соответствия ID жанра и его названия
     */
    public void setGenreMap(Map<Integer, String> genreMap) {
        this.genreMap = genreMap;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder для сериала
     */
    class TvShowViewHolder extends RecyclerView.ViewHolder {
        private final ImageView posterImageView;
        private final TextView titleTextView;
        private final TextView genresTextView;
        private final TextView releaseDateTextView;
        private final TextView ratingTextView;
        private final TextView seasonsTextView;

        public TvShowViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.iv_tv_show_poster);
            titleTextView = itemView.findViewById(R.id.tv_tv_show_title);
            genresTextView = itemView.findViewById(R.id.tv_tv_show_genres);
            releaseDateTextView = itemView.findViewById(R.id.tv_tv_show_release_date);
            ratingTextView = itemView.findViewById(R.id.tv_tv_show_rating);
            seasonsTextView = itemView.findViewById(R.id.tv_tv_show_seasons);
        }

        public void bind(final TvShow tvShow, final OnTvShowClickListener listener) {
            titleTextView.setText(tvShow.getName());
            
            // Загружаем постер с помощью Glide
            if (tvShow.getPosterUrl() != null) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                
                Glide.with(context)
                        .load(tvShow.getPosterUrl())
                        .apply(requestOptions)
                        .placeholder(R.drawable.placeholder_poster)
                        .error(R.drawable.placeholder_poster)
                        .into(posterImageView);
            } else {
                posterImageView.setImageResource(R.drawable.placeholder_poster);
            }

            // Отображаем жанры
            if (genreMap != null) {
                genresTextView.setText(tvShow.getGenresString(genreMap));
            } else {
                genresTextView.setText("");
            }

            // Дата выхода
            releaseDateTextView.setText(tvShow.getFormattedFirstAirDate());
            
            // Рейтинг
            ratingTextView.setText(context.getString(R.string.rating_format, tvShow.getVoteAverage()));
            
            // Информация о сезонах
            seasonsTextView.setText(tvShow.getFormattedSeasonInfo());

            // Обработка клика на элемент
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTvShowClick(tvShow);
                }
            });
        }
    }
}