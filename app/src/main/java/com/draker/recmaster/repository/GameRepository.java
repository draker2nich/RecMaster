package com.draker.recmaster.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.draker.recmaster.api.ApiClient;
import com.draker.recmaster.api.RawgGamesApi;
import com.draker.recmaster.model.Game;
import com.draker.recmaster.model.GameResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Репозиторий для работы с данными об играх
 */
public class GameRepository {

    private static final String TAG = "GameRepository";
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final RawgGamesApi rawgGamesApi;
    private final String apiKey;
    private static GameRepository instance;

    private GameRepository() {
        rawgGamesApi = ApiClient.getRawgGamesApi();
        apiKey = ApiClient.getRawgApiKey();
        Log.d(TAG, "GameRepository initialized");
    }

    public static synchronized GameRepository getInstance() {
        if (instance == null) {
            instance = new GameRepository();
        }
        return instance;
    }

    /**
     * Получение списка популярных игр
     */
    public void getPopularGames(MutableLiveData<List<Game>> gamesLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Fetching popular games, page: " + page);
        String ordering = "-rating"; // По убыванию рейтинга
        rawgGamesApi.getGames(apiKey, page, DEFAULT_PAGE_SIZE, ordering).enqueue(new Callback<GameResponse>() {
            @Override
            public void onResponse(Call<GameResponse> call, Response<GameResponse> response) {
                Log.d(TAG, "Popular games response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Game> games = response.body().getResults();
                    Log.d(TAG, "Got " + games.size() + " popular games");
                    gamesLiveData.setValue(games);
                } else {
                    String errorMsg = "Ошибка загрузки игр: " + response.code();
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
            public void onFailure(Call<GameResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети: " + t.getMessage();
                Log.e(TAG, "Failed to get popular games", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Поиск игр по запросу
     */
    public void searchGames(String query, MutableLiveData<List<Game>> gamesLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Searching games with query: " + query + ", page: " + page);
        rawgGamesApi.searchGames(apiKey, query, page, DEFAULT_PAGE_SIZE).enqueue(new Callback<GameResponse>() {
            @Override
            public void onResponse(Call<GameResponse> call, Response<GameResponse> response) {
                Log.d(TAG, "Search games response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Game> games = response.body().getResults();
                    Log.d(TAG, "Found " + games.size() + " games for query: " + query);
                    gamesLiveData.setValue(games);
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
            public void onFailure(Call<GameResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети при поиске: " + t.getMessage();
                Log.e(TAG, "Failed to search games", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение игр по жанру
     */
    public void getGamesByGenre(String genre, MutableLiveData<List<Game>> gamesLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Fetching games by genre: " + genre + ", page: " + page);
        rawgGamesApi.getGamesByGenre(apiKey, genre, page, DEFAULT_PAGE_SIZE).enqueue(new Callback<GameResponse>() {
            @Override
            public void onResponse(Call<GameResponse> call, Response<GameResponse> response) {
                Log.d(TAG, "Games by genre response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Game> games = response.body().getResults();
                    Log.d(TAG, "Found " + games.size() + " games for genre: " + genre);
                    gamesLiveData.setValue(games);
                } else {
                    String errorMsg = "Ошибка загрузки игр по жанру: " + response.code();
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
            public void onFailure(Call<GameResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети при загрузке игр по жанру: " + t.getMessage();
                Log.e(TAG, "Failed to get games by genre", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение игр по платформе
     */
    public void getGamesByPlatform(String platform, MutableLiveData<List<Game>> gamesLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Fetching games by platform: " + platform + ", page: " + page);
        rawgGamesApi.getGamesByPlatform(apiKey, platform, page, DEFAULT_PAGE_SIZE).enqueue(new Callback<GameResponse>() {
            @Override
            public void onResponse(Call<GameResponse> call, Response<GameResponse> response) {
                Log.d(TAG, "Games by platform response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Game> games = response.body().getResults();
                    Log.d(TAG, "Found " + games.size() + " games for platform: " + platform);
                    gamesLiveData.setValue(games);
                } else {
                    String errorMsg = "Ошибка загрузки игр по платформе: " + response.code();
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
            public void onFailure(Call<GameResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети при загрузке игр по платформе: " + t.getMessage();
                Log.e(TAG, "Failed to get games by platform", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение предстоящих игр
     */
    public void getUpcomingGames(MutableLiveData<List<Game>> gamesLiveData, MutableLiveData<String> errorLiveData, int page) {
        Log.d(TAG, "Fetching upcoming games, page: " + page);
        
        // Получаем текущую дату и дату через 6 месяцев
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String currentDate = dateFormat.format(new Date());
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 6);
        String futureDate = dateFormat.format(calendar.getTime());
        
        String dateRange = currentDate + "," + futureDate;
        String ordering = "-added"; // По убыванию даты добавления
        
        rawgGamesApi.getUpcomingGames(apiKey, dateRange, ordering, page, DEFAULT_PAGE_SIZE).enqueue(new Callback<GameResponse>() {
            @Override
            public void onResponse(Call<GameResponse> call, Response<GameResponse> response) {
                Log.d(TAG, "Upcoming games response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Game> games = response.body().getResults();
                    Log.d(TAG, "Found " + games.size() + " upcoming games");
                    gamesLiveData.setValue(games);
                } else {
                    String errorMsg = "Ошибка загрузки предстоящих игр: " + response.code();
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
            public void onFailure(Call<GameResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети при загрузке предстоящих игр: " + t.getMessage();
                Log.e(TAG, "Failed to get upcoming games", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение детальной информации об игре
     */
    public void getGameDetails(int gameId, MutableLiveData<Game> gameLiveData, MutableLiveData<String> errorLiveData) {
        Log.d(TAG, "Fetching game details for ID: " + gameId);
        rawgGamesApi.getGameDetails(gameId, apiKey).enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                Log.d(TAG, "Game details response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    Game game = response.body();
                    Log.d(TAG, "Got details for game: " + game.getName());
                    gameLiveData.setValue(game);
                } else {
                    String errorMsg = "Ошибка загрузки деталей игры: " + response.code();
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
            public void onFailure(Call<Game> call, Throwable t) {
                String errorMsg = "Ошибка сети при загрузке деталей игры: " + t.getMessage();
                Log.e(TAG, "Failed to get game details", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }
}