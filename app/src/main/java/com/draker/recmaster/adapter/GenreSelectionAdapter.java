package com.draker.recmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.draker.recmaster.R;
import com.draker.recmaster.model.Genre;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Адаптер для выбора жанров фильмов
 */
public class GenreSelectionAdapter extends RecyclerView.Adapter<GenreSelectionAdapter.GenreViewHolder> {

    private final List<Genre> genres;
    private final Set<Integer> selectedGenreIds;
    private final Context context;

    public GenreSelectionAdapter(Context context) {
        this.context = context;
        this.genres = new ArrayList<>();
        this.selectedGenreIds = new HashSet<>();
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_genre_selection, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = genres.get(position);
        holder.bind(genre);
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public void setGenres(List<Genre> genres) {
        this.genres.clear();
        if (genres != null) {
            this.genres.addAll(genres);
        }
        notifyDataSetChanged();
    }

    public void setSelectedGenreIds(List<Integer> selectedGenreIds) {
        this.selectedGenreIds.clear();
        if (selectedGenreIds != null) {
            this.selectedGenreIds.addAll(selectedGenreIds);
        }
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedGenreIds() {
        return new ArrayList<>(selectedGenreIds);
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {
        private final TextView genreName;
        private final CheckBox genreCheckbox;

        GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            genreName = itemView.findViewById(R.id.text_genre_name);
            genreCheckbox = itemView.findViewById(R.id.checkbox_genre);
        }

        void bind(final Genre genre) {
            genreName.setText(genre.getName());
            genreCheckbox.setChecked(selectedGenreIds.contains(genre.getId()));

            // Обработка нажатия на весь элемент для выбора/отмены жанра
            itemView.setOnClickListener(v -> {
                boolean isChecked = !genreCheckbox.isChecked();
                genreCheckbox.setChecked(isChecked);
                
                if (isChecked) {
                    selectedGenreIds.add(genre.getId());
                } else {
                    selectedGenreIds.remove(genre.getId());
                }
            });

            // Обработка нажатия на чекбокс
            genreCheckbox.setOnClickListener(v -> {
                boolean isChecked = genreCheckbox.isChecked();
                
                if (isChecked) {
                    selectedGenreIds.add(genre.getId());
                } else {
                    selectedGenreIds.remove(genre.getId());
                }
            });
        }
    }
}