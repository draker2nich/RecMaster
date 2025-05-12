package com.draker.recmaster.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draker.recmaster.model.Movie;
import com.draker.recmaster.repository.MovieRepository;

import java.util.List;
import java.util.Map;

/**
 * ViewModel для работы с данными о фильмах
 */
public class MovieViewModel extends ViewModel {

    private final MovieRepository repository;
    private final MutableLiveData<List<Movie>> popularMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> topRatedMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> nowPlayingMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> upcomingMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<Movie> movieDetails = new MutableLiveData<>();
    private final MutableLiveData<Map<Integer, String>> genreMap = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public MovieViewModel() {
        repository = MovieRepository.getInstance();
    }

    // Методы для получения LiveData объектов
    public LiveData<List<Movie>> getPopularMovies() {
        return popularMovies;
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        return topRatedMovies;
    }

    public LiveData<List<Movie>> getNowPlayingMovies() {
        return nowPlayingMovies;
    }

    public LiveData<List<Movie>> getUpcomingMovies() {
        return upcomingMovies;
    }

    public LiveData<List<Movie>> getSearchResults() {
        return searchResults;
    }

    public LiveData<Movie> getMovieDetails() {
        return movieDetails;
    }

    public LiveData<Map<Integer, String>> getGenreMap() {
        return genreMap;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    // Методы для загрузки данных
    public void loadPopularMovies(int page) {
        isLoading.setValue(true);
        repository.getPopularMovies(popularMovies, errorMessage, page);
        isLoading.setValue(false);
    }

    public void loadTopRatedMovies(int page) {
        isLoading.setValue(true);
        repository.getTopRatedMovies(topRatedMovies, errorMessage, page);
        isLoading.setValue(false);
    }

    public void loadNowPlayingMovies(int page) {
        isLoading.setValue(true);
        repository.getNowPlayingMovies(nowPlayingMovies, errorMessage, page);
        isLoading.setValue(false);
    }

    public void loadUpcomingMovies(int page) {
        isLoading.setValue(true);
        repository.getUpcomingMovies(upcomingMovies, errorMessage, page);
        isLoading.setValue(false);
    }

    // Этот метод пока не используется, закомментируем его до тех пор, 
    // пока не будет реализован экран деталей фильма
    /*
    public void loadMovieDetails(int movieId) {
        isLoading.setValue(true);
        repository.getMovieDetails(movieId, movieDetails, errorMessage);
        isLoading.setValue(false);
    }
    */

    public void loadGenres() {
        repository.getMovieGenres(genreMap, errorMessage);
    }

    public void searchMovies(String query, int page) {
        if (query != null && !query.trim().isEmpty()) {
            isLoading.setValue(true);
            repository.searchMovies(query, searchResults, errorMessage, page);
            isLoading.setValue(false);
        } else {
            searchResults.setValue(null);
        }
    }

    // Метод для преобразования ID жанра в название
    public String getGenreName(int genreId) {
        if (genreMap.getValue() != null) {
            return genreMap.getValue().get(genreId);
        }
        return null;
    }
}