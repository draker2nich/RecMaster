package com.draker.recmaster.util;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
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
import com.draker.recmaster.service.GamificationService;
import com.draker.recmaster.viewmodel.WatchHistoryViewModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Утилитный класс для работы с отметкой фильмов как просмотренных
 */
public class MovieWatchUtil {
    
    private static final String TAG = "MovieWatchUtil";

    // Маппинг ID жанров на их названия
    private static final Map<Integer, String> genreMap = new HashMap<Integer, String>() {{
        put(28, "Action");       // Боевик
        put(12, "Adventure");    // Приключения
        put(16, "Animation");    // Мультфильм
        put(35, "Comedy");       // Комедия
        put(80, "Crime");        // Криминал
        put(99, "Documentary");  // Документальный
        put(18, "Drama");        // Драма
        put(10751, "Family");    // Семейный
        put(14, "Fantasy");      // Фэнтези
        put(36, "History");      // Исторический
        put(27, "Horror");       // Ужасы
        put(10402, "Music");     // Музыкальный
        put(9648, "Mystery");    // Детектив
        put(10749, "Romance");   // Мелодрама
        put(878, "Sci-Fi");      // Научная фантастика
        put(10770, "TV Movie");  // ТВ фильм
        put(53, "Thriller");     // Триллер
        put(10752, "War");       // Военный
        put(37, "Western");      // Вестерн
    }};

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
            
            // Вызываем сервис геймификации для начисления очков и проверки достижений
            // Получаем главный жанр фильма, если известны ID жанров
            String mainGenre = null;
            if (movie.getGenreIds() != null && !movie.getGenreIds().isEmpty()) {
                Integer genreId = movie.getGenreIds().get(0);
                mainGenre = genreMap.get(genreId);
                Log.d(TAG, "Main genre detected: " + mainGenre + " from ID: " + genreId);
            } else {
                Log.d(TAG, "No genres available for movie: " + movie.getTitle());
            }
            
            // Записываем просмотр фильма в систему геймификации
            GamificationService.getInstance(context).recordMovieWatched(
                    currentUsername, 
                    movie.getId(), 
                    mainGenre
            );
            
            // Показываем сообщение и закрываем диалог
            Toast.makeText(context, R.string.rating_saved, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        
        // Показываем диалог
        dialog.show();
    }
}