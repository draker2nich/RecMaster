package com.draker.recmaster.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.draker.recmaster.R;
import com.draker.recmaster.model.Movie;
import com.draker.recmaster.model.Mood;
import com.draker.recmaster.model.RecommendationFilter;
import com.draker.recmaster.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ViewModel для экрана рекомендаций
 */
public class RecommendationsViewModel extends AndroidViewModel {
    private static final String TAG = "RecommendationsVM";
    
    private final MovieViewModel movieViewModel;
    private final MovieRepository repository;
    private final MediatorLiveData<List<Movie>> recommendedMovies = new MediatorLiveData<>();
    private final MutableLiveData<RecommendationFilter> currentFilter = new MutableLiveData<>(new RecommendationFilter());
    private final MutableLiveData<List<Mood>> availableMoods = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    public RecommendationsViewModel(@NonNull Application application) {
        super(application);
        repository = MovieRepository.getInstance();
        repository.setLocalRepository(application); // Устанавливаем локальный репозиторий
        
        // Создаем MovieViewModel напрямую, так как у нас есть Application
        movieViewModel = new MovieViewModel(application);
        
        // Создание списка доступных настроений
        initMoods();
        
        // Добавляем источники данных для рекомендаций
        recommendedMovies.addSource(movieViewModel.getPopularMovies(), movies -> {
            applyFilters();
        });
        
        recommendedMovies.addSource(movieViewModel.getTopRatedMovies(), movies -> {
            applyFilters();
        });
        
        recommendedMovies.addSource(currentFilter, filter -> {
            applyFilters();
        });
        
        // Загрузка данных при инициализации
        loadInitialData();
    }
    
    /**
     * Инициализация списка настроений с иконками
     */
    private void initMoods() {
        List<Mood> moods = new ArrayList<>();
        moods.add(new Mood(RecommendationFilter.MOOD_HAPPY, "Счастливое", R.drawable.ic_mood_happy));
        moods.add(new Mood(RecommendationFilter.MOOD_SAD, "Грустное", R.drawable.ic_mood_sad));
        moods.add(new Mood(RecommendationFilter.MOOD_EXCITED, "Восторженное", R.drawable.ic_mood_excited));
        moods.add(new Mood(RecommendationFilter.MOOD_RELAXED, "Расслабленное", R.drawable.ic_mood_relaxed));
        
        availableMoods.setValue(moods);
    }
    
    /**
     * Загрузка начальных данных
     */
    private void loadInitialData() {
        movieViewModel.loadGenres();
        movieViewModel.loadPopularMovies(1);
        movieViewModel.loadTopRatedMovies(1);
    }
    
    /**
     * Применение фильтров к имеющимся фильмам
     */
    private void applyFilters() {
        List<Movie> allMovies = new ArrayList<>();
        RecommendationFilter filter = currentFilter.getValue();
        
        // Получаем все фильмы из разных источников
        List<Movie> popularMovies = movieViewModel.getPopularMovies().getValue();
        if (popularMovies != null) {
            allMovies.addAll(popularMovies);
        }
        
        List<Movie> topRatedMovies = movieViewModel.getTopRatedMovies().getValue();
        if (topRatedMovies != null) {
            allMovies.addAll(topRatedMovies);
        }
        
        // Применяем фильтры
        if (filter != null && !allMovies.isEmpty()) {
            List<Movie> filtered = filter.filter(allMovies);
            Log.d(TAG, "Filtered " + allMovies.size() + " movies to " + filtered.size() 
                    + " with filter: " + filter);
            recommendedMovies.setValue(filtered);
        } else {
            recommendedMovies.setValue(allMovies);
        }
    }
    
    // Геттеры
    public LiveData<List<Movie>> getRecommendedMovies() {
        return recommendedMovies;
    }
    
    public LiveData<Map<Integer, String>> getGenreMap() {
        return movieViewModel.getGenreMap();
    }
    
    public LiveData<RecommendationFilter> getCurrentFilter() {
        return currentFilter;
    }
    
    public LiveData<List<Mood>> getAvailableMoods() {
        return availableMoods;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    // Методы для обновления фильтров
    
    /**
     * Установка настроения
     */
    public void setMood(String mood) {
        RecommendationFilter filter = currentFilter.getValue();
        if (filter != null) {
            filter.setMood(mood);
            currentFilter.setValue(filter);
        }
    }
    
    /**
     * Установка максимальной продолжительности
     */
    public void setMaxDuration(int maxDuration) {
        RecommendationFilter filter = currentFilter.getValue();
        if (filter != null) {
            filter.setMaxDuration(maxDuration);
            currentFilter.setValue(filter);
        }
    }
    
    /**
     * Установка выбранных жанров
     */
    public void setSelectedGenres(List<Integer> genreIds) {
        RecommendationFilter filter = currentFilter.getValue();
        if (filter != null) {
            filter.setSelectedGenreIds(genreIds);
            currentFilter.setValue(filter);
        }
    }
    
    /**
     * Добавление жанра в фильтр
     */
    public void addGenreToFilter(int genreId) {
        RecommendationFilter filter = currentFilter.getValue();
        if (filter != null) {
            filter.addGenreId(genreId);
            currentFilter.setValue(filter);
        }
    }
    
    /**
     * Удаление жанра из фильтра
     */
    public void removeGenreFromFilter(int genreId) {
        RecommendationFilter filter = currentFilter.getValue();
        if (filter != null) {
            filter.removeGenreId(genreId);
            currentFilter.setValue(filter);
        }
    }
    
    /**
     * Сброс всех фильтров
     */
    public void resetFilters() {
        currentFilter.setValue(new RecommendationFilter());
    }
}