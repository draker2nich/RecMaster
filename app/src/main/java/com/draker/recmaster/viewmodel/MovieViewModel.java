package com.draker.recmaster.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.draker.recmaster.model.Movie;
import com.draker.recmaster.repository.MovieRepository;

import java.util.List;
import java.util.Map;

/**
 * ViewModel для работы с данными о фильмах
 */
public class MovieViewModel extends AndroidViewModel {

    private static final String TAG = "MovieViewModel";

    private final MovieRepository movieRepository;
    private final MutableLiveData<List<Movie>> popularMoviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> topRatedMoviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> upcomingMoviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> nowPlayingMoviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Map<Integer, String>> genreMapLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>(false);

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance();
        movieRepository.setLocalRepository(application);
    }

    /**
     * Загрузка списка популярных фильмов
     */
    public void loadPopularMovies(int page) {
        isLoadingLiveData.setValue(true);
        movieRepository.getPopularMovies(popularMoviesLiveData, errorMessageLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Загрузка списка фильмов с высоким рейтингом
     */
    public void loadTopRatedMovies(int page) {
        isLoadingLiveData.setValue(true);
        movieRepository.getTopRatedMovies(topRatedMoviesLiveData, errorMessageLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Загрузка списка фильмов, которые скоро выйдут
     */
    public void loadUpcomingMovies(int page) {
        isLoadingLiveData.setValue(true);
        movieRepository.getUpcomingMovies(upcomingMoviesLiveData, errorMessageLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Загрузка списка фильмов, которые сейчас в кино
     */
    public void loadNowPlayingMovies(int page) {
        isLoadingLiveData.setValue(true);
        movieRepository.getNowPlayingMovies(nowPlayingMoviesLiveData, errorMessageLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Загрузка жанров фильмов
     */
    public void loadGenres() {
        movieRepository.getMovieGenres(genreMapLiveData, errorMessageLiveData);
    }

    /**
     * Поиск фильмов по запросу
     */
    public void searchMovies(String query, int page) {
        isLoadingLiveData.setValue(true);
        MutableLiveData<List<Movie>> searchResultsLiveData = new MutableLiveData<>();
        movieRepository.searchMovies(query, searchResultsLiveData, errorMessageLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получить LiveData со списком популярных фильмов
     */
    public LiveData<List<Movie>> getPopularMovies() {
        return popularMoviesLiveData;
    }

    /**
     * Получить LiveData со списком фильмов с высоким рейтингом
     */
    public LiveData<List<Movie>> getTopRatedMovies() {
        return topRatedMoviesLiveData;
    }

    /**
     * Получить LiveData со списком предстоящих фильмов
     */
    public LiveData<List<Movie>> getUpcomingMovies() {
        return upcomingMoviesLiveData;
    }

    /**
     * Получить LiveData со списком фильмов, которые сейчас в кино
     */
    public LiveData<List<Movie>> getNowPlayingMovies() {
        return nowPlayingMoviesLiveData;
    }

    /**
     * Получить LiveData с картой жанров
     */
    public LiveData<Map<Integer, String>> getGenreMap() {
        return genreMapLiveData;
    }

    /**
     * Получить LiveData с сообщениями об ошибках
     */
    public LiveData<String> getErrorMessage() {
        return errorMessageLiveData;
    }

    /**
     * Получить LiveData с состоянием загрузки
     */
    public LiveData<Boolean> isLoading() {
        return isLoadingLiveData;
    }
}