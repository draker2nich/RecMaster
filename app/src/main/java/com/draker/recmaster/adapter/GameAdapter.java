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
import com.draker.recmaster.model.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер для отображения списка игр в RecyclerView
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private final Context context;
    private final List<Game> games;
    private final OnGameClickListener listener;

    /**
     * Интерфейс для обработки событий клика на игру
     */
    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

    /**
     * Конструктор адаптера
     * @param context контекст активности
     * @param listener слушатель кликов
     */
    public GameAdapter(Context context, OnGameClickListener listener) {
        this.context = context;
        this.games = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = games.get(position);
        holder.bind(game, listener);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    /**
     * Обновляет список игр
     * @param newGames новый список игр
     */
    public void setGames(List<Game> newGames) {
        this.games.clear();
        if (newGames != null) {
            this.games.addAll(newGames);
        }
        notifyDataSetChanged();
    }

    /**
     * ViewHolder для игры
     */
    class GameViewHolder extends RecyclerView.ViewHolder {
        private final ImageView coverImageView;
        private final TextView titleTextView;
        private final TextView genresTextView;
        private final TextView platformsTextView;
        private final TextView releaseDateTextView;
        private final TextView ratingTextView;
        private final TextView metacriticTextView;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImageView = itemView.findViewById(R.id.iv_game_cover);
            titleTextView = itemView.findViewById(R.id.tv_game_title);
            genresTextView = itemView.findViewById(R.id.tv_game_genres);
            platformsTextView = itemView.findViewById(R.id.tv_game_platforms);
            releaseDateTextView = itemView.findViewById(R.id.tv_game_release_date);
            ratingTextView = itemView.findViewById(R.id.tv_game_rating);
            metacriticTextView = itemView.findViewById(R.id.tv_game_metacritic);
        }

        public void bind(final Game game, final OnGameClickListener listener) {
            titleTextView.setText(game.getName());
            
            // Загружаем изображение с помощью Glide
            if (game.getBackgroundImage() != null && !game.getBackgroundImage().isEmpty()) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                
                Glide.with(context)
                        .load(game.getBackgroundImage())
                        .apply(requestOptions)
                        .placeholder(R.drawable.placeholder_game)
                        .error(R.drawable.placeholder_game)
                        .into(coverImageView);
            } else {
                coverImageView.setImageResource(R.drawable.placeholder_game);
            }

            // Отображаем жанры
            String genres = game.getGenresString();
            if (!genres.isEmpty()) {
                genresTextView.setVisibility(View.VISIBLE);
                genresTextView.setText(genres);
            } else {
                genresTextView.setVisibility(View.GONE);
            }

            // Отображаем платформы
            String platforms = game.getPlatformsString();
            if (!platforms.isEmpty()) {
                platformsTextView.setVisibility(View.VISIBLE);
                platformsTextView.setText(platforms);
            } else {
                platformsTextView.setVisibility(View.GONE);
            }

            // Дата релиза
            if (game.getReleased() != null && !game.getReleased().isEmpty()) {
                releaseDateTextView.setVisibility(View.VISIBLE);
                releaseDateTextView.setText(game.getFormattedReleaseDate());
            } else {
                releaseDateTextView.setVisibility(View.GONE);
            }

            // Рейтинг
            if (game.getRating() > 0) {
                ratingTextView.setVisibility(View.VISIBLE);
                ratingTextView.setText(context.getString(R.string.rating_format, game.getRating()));
            } else {
                ratingTextView.setVisibility(View.GONE);
            }

            // Метакритик
            if (game.getMetacritic() > 0) {
                metacriticTextView.setVisibility(View.VISIBLE);
                metacriticTextView.setText(context.getString(R.string.metacritic_format, game.getMetacritic()));
            } else {
                metacriticTextView.setVisibility(View.GONE);
            }

            // Обработка клика на элемент
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onGameClick(game);
                }
            });
        }
    }
}