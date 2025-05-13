package com.draker.recmaster.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.draker.recmaster.model.Game;
import com.draker.recmaster.repository.GameRepository;

import java.util.List;

/**
 * ViewModel для работы с данными об играх
 */
public class GameViewModel extends AndroidViewModel {

    private static final String TAG = "GameViewModel";

    private final GameRepository gameRepository;
    private final MutableLiveData<List<Game>> gamesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Game> gameDetailsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>(false);

    public GameViewModel(@NonNull Application application) {
        super(application);
        gameRepository = GameRepository.getInstance();
    }

    /**
     * Получение списка популярных игр
     */
    public void getPopularGames(int page) {
        isLoadingLiveData.setValue(true);
        gameRepository.getPopularGames(gamesLiveData, errorLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Поиск игр по запросу
     */
    public void searchGames(String query, int page) {
        isLoadingLiveData.setValue(true);
        gameRepository.searchGames(query, gamesLiveData, errorLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получение игр по жанру
     */
    public void getGamesByGenre(String genre, int page) {
        isLoadingLiveData.setValue(true);
        gameRepository.getGamesByGenre(genre, gamesLiveData, errorLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получение игр по платформе
     */
    public void getGamesByPlatform(String platform, int page) {
        isLoadingLiveData.setValue(true);
        gameRepository.getGamesByPlatform(platform, gamesLiveData, errorLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получение предстоящих игр
     */
    public void getUpcomingGames(int page) {
        isLoadingLiveData.setValue(true);
        gameRepository.getUpcomingGames(gamesLiveData, errorLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получение детальной информации об игре
     */
    public void getGameDetails(int gameId) {
        isLoadingLiveData.setValue(true);
        gameRepository.getGameDetails(gameId, gameDetailsLiveData, errorLiveData);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получить LiveData со списком игр
     */
    public MutableLiveData<List<Game>> getGamesLiveData() {
        return gamesLiveData;
    }

    /**
     * Получить LiveData с деталями игры
     */
    public MutableLiveData<Game> getGameDetailsLiveData() {
        return gameDetailsLiveData;
    }

    /**
     * Получить LiveData с ошибками
     */
    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    /**
     * Получить LiveData с состоянием загрузки
     */
    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }
}