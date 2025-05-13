package com.draker.recmaster.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.draker.recmaster.database.entity.MovieEntity;
import com.draker.recmaster.database.entity.WatchHistoryEntity;
import com.draker.recmaster.database.repository.LocalMovieRepository;
import com.draker.recmaster.database.repository.UserRepository;
import com.draker.recmaster.model.Movie;
import com.draker.recmaster.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel для работы с историей просмотров
 */
public class WatchHistoryViewModel extends AndroidViewModel {
    
    private final LocalMovieRepository localMovieRepository;
    private final UserRepository userRepository;
    private String currentUsername;
    
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    public WatchHistoryViewModel(@NonNull Application application) {
        super(application);
        localMovieRepository = LocalMovieRepository.getInstance(application);
        userRepository = UserRepository.getInstance(application);
    }
    
    /**
     * Установка текущего пользователя
     */
    public void setCurrentUser(String username) {
        this.currentUsername = username;
    }
    
    /**
     * Получение списка просмотренных фильмов
     */
    public LiveData<List<MovieEntity>> getWatchedMovies() {
        return localMovieRepository.getWatchedMovies();
    }
    
    /**
     * Получение истории просмотров для текущего пользователя
     */
    public LiveData<List<WatchHistoryEntity>> getWatchHistory() {
        if (currentUsername == null || currentUsername.isEmpty()) {
            return new MutableLiveData<>(new ArrayList<>());
        }
        return localMovieRepository.getWatchHistoryForUser(currentUsername);
    }
    
    /**
     * Сохранение фильма в локальную базу данных
     */
    public void saveMovieToDatabase(Movie movie) {
        MovieEntity entity = localMovieRepository.movieToEntity(movie);
        localMovieRepository.insertMovie(entity);
    }
    
    /**
     * Сохранение списка фильмов в локальную базу данных
     */
    public void saveMoviesToDatabase(List<Movie> movies) {
        List<MovieEntity> entities = localMovieRepository.moviesToEntities(movies);
        localMovieRepository.insertMovies(entities);
    }
    
    /**
     * Отметить фильм как просмотренный
     */
    public void markMovieAsWatched(int movieId, int rating, String notes) {
        if (currentUsername == null || currentUsername.isEmpty()) {
            errorMessage.setValue("Необходимо войти в систему для отметки просмотренных фильмов");
            return;
        }
        
        // Добавляем в историю просмотров и начисляем опыт
        localMovieRepository.markMovieAsWatched(movieId, currentUsername, rating, notes);
        
        // Опыт рассчитывается как: базовый опыт (20) + бонус за рейтинг (rating * 5)
        int experienceGained = 20 + (rating * 5);
        userRepository.addExperience(currentUsername, experienceGained);
    }
    
    /**
     * Получение сообщения об ошибке
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}