package com.draker.recmaster.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.draker.recmaster.R;
import com.draker.recmaster.data.UserPreferences;
import com.draker.recmaster.model.Movie;
import com.draker.recmaster.viewmodel.WatchHistoryViewModel;

/**
 * Утилитный класс для работы с отметкой фильмов как просмотренных
 */
public class MovieWatchUtil {

    /**
     * Показывает диалог для отметки фильма как просмотренного
     */
    public static void showMarkAsWatchedDialog(Context context, Movie movie, WatchHistoryViewModel viewModel) {
        UserPreferences userPreferences = new UserPreferences(context);
        String currentUsername = userPreferences.getUsername();
        
        // Проверка, вошел ли пользователь
        if (currentUsername == null || currentUsername.isEmpty()) {
            Toast.makeText(context, R.string.must_login, Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Создаем диалог
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mark_watched, null);
        builder.setView(dialogView);
        
        // Находим элементы диалога
        TextView tvMovieTitle = dialogView.findViewById(R.id.tvMovieTitle);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        EditText etNotes = dialogView.findViewById(R.id.etNotes);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        
        // Устанавливаем название фильма
        tvMovieTitle.setText(movie.getTitle());
        
        // Создаем диалог
        AlertDialog dialog = builder.create();
        
        // Обработчик для кнопки "Отмена"
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        // Обработчик для кнопки "Сохранить"
        btnSave.setOnClickListener(v -> {
            // Получаем рейтинг и заметки
            int rating = Math.round(ratingBar.getRating());
            String notes = etNotes.getText().toString().trim();
            
            // Сохраняем данные в базу
            viewModel.markMovieAsWatched(movie.getId(), rating, notes);
            
            // Сохраняем фильм в базу данных (на случай если его там еще нет)
            viewModel.saveMovieToDatabase(movie);
            
            // Показываем сообщение и закрываем диалог
            Toast.makeText(context, R.string.rating_saved, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        
        // Показываем диалог
        dialog.show();
    }
}