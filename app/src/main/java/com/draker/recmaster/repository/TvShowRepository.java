package com.draker.recmaster.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.draker.recmaster.api.ApiClient;
import com.draker.recmaster.api.TvShowApi;
import com.draker.recmaster.database.repository.LocalTvShowRepository;
import com.draker.recmaster.model.Genre;
import com.draker.recmaster.model.GenreResponse;
import com.draker.recmaster.model.TvShow;
import com.draker.recmaster.model.TvShowResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Репозиторий для работы с данными о сериалах
 */
public class TvShowRepository {

    private static final String TAG = "TvShowRepository";
    private static final String LANGUAGE = "ru-RU";

    private final TvShowApi tvShowApi;
    private static TvShowRepository instance;
    private LocalTvShowRepository localTvShowRepository;
    
    // Кэш жанров для быстрого доступа
    private final Map<Integer, String> genreMap = new HashMap<>();

    private TvShowRepository() {
        tvShowApi = ApiClient.getTvShowApi();
        Log.d(TAG, "TvShowRepository initialized");
    }

    public static synchronized TvShowRepository getInstance() {
        if (instance == null) {
            instance = new TvShowRepository();
        }
        return instance;
    }

    /**
     * Устанавливает экземпляр LocalTvShowRepository для сохранения данных в базу
     */
    public void setLocalRepository(Application application) {
        if (localTvShowRepository == null) {
            localTvShowRepository = LocalTvShowRepository.getInstance(application);
        }
    }

    /**
     * Получение списка популярных сериалов
     */
    public void getPopularTvShows(MutableLiveData<List<TvShow>> tvShowsLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Fetching popular TV shows, page: " + page);
        tvShowApi.getPopularTvShows(LANGUAGE, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                Log.d(TAG, "Popular TV shows response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<TvShow> tvShows = response.body().getResults();
                    Log.d(TAG, "Got " + tvShows.size() + " popular TV shows");
                    tvShowsLiveData.setValue(tvShows);
                    
                    // Сохраняем полученные сериалы в локальную базу данных
                    if (localTvShowRepository != null) {
                        localTvShowRepository.insertTvShows(localTvShowRepository.tvShowsToEntities(tvShows));
                    }
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
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети: " + t.getMessage();
                Log.e(TAG, "Failed to get popular TV shows", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение списка сериалов с высоким рейтингом
     */
    public void getTopRatedTvShows(MutableLiveData<List<TvShow>> tvShowsLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Fetching top rated TV shows, page: " + page);
        tvShowApi.getTopRatedTvShows(LANGUAGE, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                Log.d(TAG, "Top rated TV shows response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<TvShow> tvShows = response.body().getResults();
                    Log.d(TAG, "Got " + tvShows.size() + " top rated TV shows");
                    tvShowsLiveData.setValue(tvShows);
                    
                    // Сохраняем полученные сериалы в локальную базу данных
                    if (localTvShowRepository != null) {
                        localTvShowRepository.insertTvShows(localTvShowRepository.tvShowsToEntities(tvShows));
                    }
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
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети: " + t.getMessage();
                Log.e(TAG, "Failed to get top rated TV shows", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение списка сериалов, которые сейчас в эфире
     */
    public void getOnTheAirTvShows(MutableLiveData<List<TvShow>> tvShowsLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Fetching on the air TV shows, page: " + page);
        tvShowApi.getOnTheAirTvShows(LANGUAGE, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                Log.d(TAG, "On the air TV shows response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<TvShow> tvShows = response.body().getResults();
                    Log.d(TAG, "Got " + tvShows.size() + " on the air TV shows");
                    tvShowsLiveData.setValue(tvShows);
                    
                    // Сохраняем полученные сериалы в локальную базу данных
                    if (localTvShowRepository != null) {
                        localTvShowRepository.insertTvShows(localTvShowRepository.tvShowsToEntities(tvShows));
                    }
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
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети: " + t.getMessage();
                Log.e(TAG, "Failed to get on the air TV shows", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение списка сериалов, выходящих сегодня
     */
    public void getAiringTodayTvShows(MutableLiveData<List<TvShow>> tvShowsLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Fetching airing today TV shows, page: " + page);
        tvShowApi.getAiringTodayTvShows(LANGUAGE, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                Log.d(TAG, "Airing today TV shows response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<TvShow> tvShows = response.body().getResults();
                    Log.d(TAG, "Got " + tvShows.size() + " airing today TV shows");
                    tvShowsLiveData.setValue(tvShows);
                    
                    // Сохраняем полученные сериалы в локальную базу данных
                    if (localTvShowRepository != null) {
                        localTvShowRepository.insertTvShows(localTvShowRepository.tvShowsToEntities(tvShows));
                    }
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
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети: " + t.getMessage();
                Log.e(TAG, "Failed to get airing today TV shows", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение списка жанров сериалов
     */
    public void getTvShowGenres(MutableLiveData<Map<Integer, String>> genresLiveData, MutableLiveData<String> errorLiveData) {
        if (!genreMap.isEmpty()) {
            Log.d(TAG, "Using cached TV show genres: " + genreMap.size() + " items");
            genresLiveData.setValue(genreMap);
            return;
        }

        Log.d(TAG, "Fetching TV show genres");
        tvShowApi.getTvShowGenres(LANGUAGE).enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                Log.d(TAG, "TV show genres response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Genre> genres = response.body().getGenres();
                    Log.d(TAG, "Got " + genres.size() + " TV show genres");
                    
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
                Log.e(TAG, "Failed to get TV show genres", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Поиск сериалов по запросу
     */
    public void searchTvShows(String query, MutableLiveData<List<TvShow>> tvShowsLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Searching TV shows with query: " + query + ", page: " + page);
        tvShowApi.searchTvShows(LANGUAGE, query, page, false).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                Log.d(TAG, "Search TV shows response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<TvShow> tvShows = response.body().getResults();
                    Log.d(TAG, "Found " + tvShows.size() + " TV shows for query: " + query);
                    tvShowsLiveData.setValue(tvShows);
                    
                    // Сохраняем найденные сериалы в локальную базу данных
                    if (localTvShowRepository != null) {
                        localTvShowRepository.insertTvShows(localTvShowRepository.tvShowsToEntities(tvShows));
                    }
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
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети при поиске: " + t.getMessage();
                Log.e(TAG, "Failed to search TV shows", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }
}