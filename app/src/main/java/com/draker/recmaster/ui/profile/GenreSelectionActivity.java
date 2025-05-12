package com.draker.recmaster.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.draker.recmaster.R;
import com.draker.recmaster.data.UserPreferences;
import com.draker.recmaster.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenreSelectionActivity extends AppCompatActivity implements GenreAdapter.OnGenreSelectedListener {

    private Toolbar toolbar;
    private RecyclerView recyclerGenres;
    private Button buttonSave;
    
    private UserPreferences userPreferences;
    private User currentUser;
    private GenreAdapter genreAdapter;
    private List<String> selectedGenres = new ArrayList<>();
    
    // Список всех доступных жанров
    private static final List<String> ALL_GENRES = Arrays.asList(
            "Боевик", "Комедия", "Драма", "Фантастика", "Фэнтези", 
            "Ужасы", "Триллер", "Детектив", "Мелодрама", "Приключения",
            "Документальный", "Исторический", "Биография", "Криминал", "Музыкальный",
            "Аниме", "Вестерн", "Семейный", "Спортивный"
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_selection);
        
        userPreferences = new UserPreferences(this);
        currentUser = userPreferences.loadUser();
        
        // Получаем уже выбранные жанры, если они есть
        if (currentUser != null && currentUser.getPreferredGenres() != null) {
            selectedGenres.addAll(currentUser.getPreferredGenres());
        }
        
        // Инициализация UI
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.preferred_genres);
        
        recyclerGenres = findViewById(R.id.recycler_genres);
        buttonSave = findViewById(R.id.button_save);
        
        // Настраиваем RecyclerView
        recyclerGenres.setLayoutManager(new LinearLayoutManager(this));
        genreAdapter = new GenreAdapter(ALL_GENRES, selectedGenres, this);
        recyclerGenres.setAdapter(genreAdapter);
        
        // Настраиваем обработчики
        buttonSave.setOnClickListener(v -> saveGenres());
    }
    
    @Override
    public void onGenreSelected(String genre, boolean isSelected) {
        if (isSelected) {
            if (!selectedGenres.contains(genre)) {
                selectedGenres.add(genre);
            }
        } else {
            selectedGenres.remove(genre);
        }
    }
    
    private void saveGenres() {
        if (currentUser != null) {
            currentUser.setPreferredGenres(selectedGenres);
            userPreferences.saveUser(currentUser);
            
            // Возвращаем результат в EditProfileActivity
            Intent resultIntent = new Intent();
            resultIntent.putStringArrayListExtra("selectedGenres", new ArrayList<>(selectedGenres));
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}