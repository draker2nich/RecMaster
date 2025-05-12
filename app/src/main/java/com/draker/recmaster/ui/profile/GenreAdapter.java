package com.draker.recmaster.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.draker.recmaster.R;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    
    private final List<String> genres;
    private final List<String> selectedGenres;
    private final OnGenreSelectedListener listener;
    
    public interface OnGenreSelectedListener {
        void onGenreSelected(String genre, boolean isSelected);
    }
    
    public GenreAdapter(List<String> genres, List<String> selectedGenres, OnGenreSelectedListener listener) {
        this.genres = genres;
        this.selectedGenres = selectedGenres;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        String genre = genres.get(position);
        boolean isSelected = selectedGenres.contains(genre);
        
        holder.checkBoxGenre.setText(genre);
        holder.checkBoxGenre.setChecked(isSelected);
        
        holder.checkBoxGenre.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onGenreSelected(genre, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }
    
    static class GenreViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxGenre;
        
        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxGenre = itemView.findViewById(R.id.checkbox_genre);
        }
    }
}