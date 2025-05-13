package com.draker.recmaster.database.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.draker.recmaster.database.AppDatabase;
import com.draker.recmaster.database.dao.MovieDao;
import com.draker.recmaster.database.dao.WatchHistoryDao;
import com.draker.recmaster.database.entity.MovieEntity;
import com.draker.recmaster.database.entity.WatchHistoryEntity;
import com.draker.recmaster.model.Movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Репозиторий для локальной работы с фильмами в базе данных
 */
public class LocalMovieRepository {
    
    private static final String TAG = "LocalMovieRepository";
    
    private final MovieDao movieDao;
    private final WatchHistoryDao watchHistoryDao;
    private final LiveData<List<MovieEntity>> watchedMovies;
    
    private static volatile LocalMovieRepository INSTANCE;
    
    public static LocalMovieRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (LocalMovieRepository.class) {
                if (INSTANCE == null) {
                    AppDatabase db = AppDatabase.getInstance(application);
                    INSTANCE = new LocalMovieRepository(db.movieDao(), db.watchHistoryDao());
                }
            }
        }
        return INSTANCE;
    }
    
    private LocalMovieRepository(MovieDao movieDao, WatchHistoryDao watchHistoryDao) {
        this.movieDao = movieDao;
        this.watchHistoryDao = watchHistoryDao;
        this.watchedMovies = movieDao.getWatchedMovies();
        Log.d(TAG, "LocalMovieRepository initialized");
    }
    
    /**
     * Получение всех просмотренных фильмов
     */
    public LiveData<List<MovieEntity>> getWatchedMovies() {
        return watchedMovies;
    }
    
    /**
     * Получение фильма по ID
     */
    public LiveData<MovieEntity> getMovieById(int id) {
        return movieDao.getMovieById(id);
    }
    
    /**
     * Сохранение фильма в базу данных
     */
    public void insertMovie(MovieEntity movie) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long result = movieDao.insert(movie);
            Log.d(TAG, "Inserted movie: " + movie.getTitle() + " with ID: " + result);
        });
    }
    
    /**
     * Сохранение списка фильмов в базу данных
     */
    public void insertMovies(List<MovieEntity> movies) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            movieDao.insertAll(movies);
            Log.d(TAG, "Inserted " + movies.size() + " movies into database");
        });
    }
    
    /**
     * Преобразование Movie из API в MovieEntity для базы данных
     */
    public MovieEntity movieToEntity(Movie movie) {
        MovieEntity entity = new MovieEntity();
        entity.setId(movie.getId());
        entity.setTitle(movie.getTitle());
        entity.setOverview(movie.getOverview());
        entity.setPosterPath(movie.getPosterPath());
        entity.setBackdropPath(movie.getBackdropPath());
        entity.setVoteAverage(movie.getVoteAverage());
        entity.setVoteCount(movie.getVoteCount());
        entity.setReleaseDate(movie.getReleaseDate());
        entity.setGenreIds(movie.getGenreIds());
        entity.setPopularity(movie.getPopularity());
        entity.setAdult(movie.isAdult());
        
        // По умолчанию не просмотрен
        entity.setWatched(false);
        entity.setUserRating(0);
        
        return entity;
    }
    
    /**
     * Преобразование списка Movie из API в список MovieEntity для базы данных
     */
    public List<MovieEntity> moviesToEntities(List<Movie> movies) {
        List<MovieEntity> entities = new ArrayList<>(movies.size());
        for (Movie movie : movies) {
            entities.add(movieToEntity(movie));
        }
        return entities;
    }
    
    /**
     * Преобразование MovieEntity из базы данных в Movie для UI
     */
    public Movie entityToMovie(MovieEntity entity) {
        Movie movie = new Movie();
        movie.setId(entity.getId());
        movie.setTitle(entity.getTitle());
        movie.setOverview(entity.getOverview());
        movie.setPosterPath(entity.getPosterPath());
        movie.setBackdropPath(entity.getBackdropPath());
        movie.setVoteAverage(entity.getVoteAverage());
        movie.setVoteCount(entity.getVoteCount());
        movie.setReleaseDate(entity.getReleaseDate());
        movie.setGenreIds(entity.getGenreIds());
        movie.setPopularity(entity.getPopularity());
        movie.setAdult(entity.isAdult());
        
        return movie;
    }
    
    /**
     * Отметить фильм как просмотренный
     */
    public void markMovieAsWatched(int movieId, String username, int rating, String notes) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Date now = new Date();
            
            // Обновляем статус фильма в таблице фильмов
            movieDao.updateWatchedStatus(movieId, true, now.getTime(), rating, notes);
            
            // Добавляем запись в историю просмотров
            WatchHistoryEntity historyEntity = new WatchHistoryEntity();
            historyEntity.setMovieId(movieId);
            historyEntity.setUsername(username);
            historyEntity.setWatchedDate(now);
            historyEntity.setUserRating(rating);
            historyEntity.setUserNotes(notes);
            
            // Расчет опыта на основе рейтинга (просто пример)
            int experienceGained = 20 + (rating * 5);  // Базовый опыт + бонус за рейтинг
            historyEntity.setExperienceGained(experienceGained);
            
            long historyId = watchHistoryDao.insert(historyEntity);
            Log.d(TAG, "Added to watch history with ID: " + historyId);
        });
    }
    
    /**
     * Поиск фильмов по запросу
     */
    public LiveData<List<MovieEntity>> searchMovies(String query) {
        return movieDao.searchMovies(query);
    }
    
    /**
     * Получение истории просмотров для пользователя
     */
    public LiveData<List<WatchHistoryEntity>> getWatchHistoryForUser(String username) {
        return watchHistoryDao.getWatchHistoryByUsername(username);
    }
    
    /**
     * Очистка всех данных
     */
    public void clearAllData() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            movieDao.deleteAll();
            watchHistoryDao.deleteAll();
            Log.d(TAG, "All local movie data cleared");
        });
    }
}