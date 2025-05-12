package com.draker.recmaster.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.draker.recmaster.BuildConfig;
import com.draker.recmaster.api.ApiClient;
import com.draker.recmaster.api.TmdbApi;
import com.draker.recmaster.model.Genre;
import com.draker.recmaster.model.GenreResponse;
import com.draker.recmaster.model.Movie;
import com.draker.recmaster.model.MovieResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Репозиторий для работы с данными о фильмах
 */
public class MovieRepository {

    private static final String TAG = "MovieRepository";
    private static final String LANGUAGE = "ru-RU";

    private final TmdbApi tmdbApi;
    private static MovieRepository instance;
    
    // Кэш жанров для быстрого доступа
    private final Map<Integer, String> genreMap = new HashMap<>();

    private MovieRepository() {
        tmdbApi = ApiClient.getTmdbApi();
        Log.d(TAG, "MovieRepository initialized");
    }

    public static synchronized MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    /**
     * Получение списка популярных фильмов
     */
    public void getPopularMovies(MutableLiveData<List<Movie>> moviesLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Fetching popular movies, page: " + page);
        tmdbApi.getPopularMovies(LANGUAGE, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "Popular movies response received. Code: " + response.code());
                Log.d(TAG, "URL: " + call.request().url());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    Log.d(TAG, "Got " + movies.size() + " popular movies");
                    moviesLiveData.setValue(movies);
                } else {
                    String errorMsg = "Ошибка загрузки данных: " + response.code();
                    Log.e(TAG, errorMsg);
                    
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            errorMsg += " - " + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    
                    errorLiveData.setValue(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети: " + t.getMessage();
                Log.e(TAG, "Failed to get popular movies", t);
                Log.e(TAG, "URL: " + call.request().url());
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение списка фильмов с высоким рейтингом
     */
    public void getTopRatedMovies(MutableLiveData<List<Movie>> moviesLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Fetching top rated movies, page: " + page);
        tmdbApi.getTopRatedMovies(LANGUAGE, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "Top rated movies response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    Log.d(TAG, "Got " + movies.size() + " top rated movies");
                    moviesLiveData.setValue(movies);
                } else {
                    String errorMsg = "Ошибка загрузки данных: " + response.code();
                    Log.e(TAG, errorMsg);
                    
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            errorMsg += " - " + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    
                    errorLiveData.setValue(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети: " + t.getMessage();
                Log.e(TAG, "Failed to get top rated movies", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение списка недавно вышедших фильмов
     */
    public void getNowPlayingMovies(MutableLiveData<List<Movie>> moviesLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Fetching now playing movies, page: " + page);
        tmdbApi.getNowPlayingMovies(LANGUAGE, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "Now playing movies response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    Log.d(TAG, "Got " + movies.size() + " now playing movies");
                    moviesLiveData.setValue(movies);
                } else {
                    String errorMsg = "Ошибка загрузки данных: " + response.code();
                    Log.e(TAG, errorMsg);
                    
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            errorMsg += " - " + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    
                    errorLiveData.setValue(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети: " + t.getMessage();
                Log.e(TAG, "Failed to get now playing movies", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение списка предстоящих фильмов
     */
    public void getUpcomingMovies(MutableLiveData<List<Movie>> moviesLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Fetching upcoming movies, page: " + page);
        tmdbApi.getUpcomingMovies(LANGUAGE, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "Upcoming movies response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    Log.d(TAG, "Got " + movies.size() + " upcoming movies");
                    moviesLiveData.setValue(movies);
                } else {
                    String errorMsg = "Ошибка загрузки данных: " + response.code();
                    Log.e(TAG, errorMsg);
                    
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            errorMsg += " - " + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    
                    errorLiveData.setValue(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети: " + t.getMessage();
                Log.e(TAG, "Failed to get upcoming movies", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение списка жанров
     */
    public void getMovieGenres(MutableLiveData<Map<Integer, String>> genresLiveData, MutableLiveData<String> errorLiveData) {
        if (!genreMap.isEmpty()) {
            Log.d(TAG, "Using cached genres: " + genreMap.size() + " items");
            genresLiveData.setValue(genreMap);
            return;
        }

        Log.d(TAG, "Fetching movie genres");
        tmdbApi.getMovieGenres(LANGUAGE).enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                Log.d(TAG, "Genres response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Genre> genres = response.body().getGenres();
                    Log.d(TAG, "Got " + genres.size() + " genres");
                    
                    for (Genre genre : genres) {
                        genreMap.put(genre.getId(), genre.getName());
                    }
                    genresLiveData.setValue(genreMap);
                } else {
                    String errorMsg = "Ошибка загрузки жанров: " + response.code();
                    Log.e(TAG, errorMsg);
                    
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            errorMsg += " - " + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    
                    errorLiveData.setValue(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети при загрузке жанров: " + t.getMessage();
                Log.e(TAG, "Failed to get genres", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Поиск фильмов по запросу
     */
    public void searchMovies(String query, MutableLiveData<List<Movie>> moviesLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Searching movies with query: " + query + ", page: " + page);
        tmdbApi.searchMovies(LANGUAGE, query, page, false).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "Search movies response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    Log.d(TAG, "Found " + movies.size() + " movies for query: " + query);
                    moviesLiveData.setValue(movies);
                } else {
                    String errorMsg = "Ошибка поиска: " + response.code();
                    Log.e(TAG, errorMsg);
                    
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            errorMsg += " - " + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    
                    errorLiveData.setValue(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети при поиске: " + t.getMessage();
                Log.e(TAG, "Failed to search movies", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }
}