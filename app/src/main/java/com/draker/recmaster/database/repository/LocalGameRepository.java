package com.draker.recmaster.database.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.draker.recmaster.database.AppDatabase;
import com.draker.recmaster.database.dao.GameDao;
import com.draker.recmaster.database.entity.GameEntity;
import com.draker.recmaster.model.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с локальной базой данных игр
 */
public class LocalGameRepository {

    private static final String TAG = "LocalGameRepo";
    private static LocalGameRepository instance;
    private final GameDao gameDao;

    private LocalGameRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        gameDao = db.gameDao();
        Log.d(TAG, "LocalGameRepository initialized");
    }

    public static synchronized LocalGameRepository getInstance(Application application) {
        if (instance == null) {
            instance = new LocalGameRepository(application);
        }
        return instance;
    }

    /**
     * Вставка игры в базу данных
     */
    public void insertGame(GameEntity gameEntity) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gameDao.insert(gameEntity);
            Log.d(TAG, "Game inserted: " + gameEntity.getName());
        });
    }

    /**
     * Вставка списка игр в базу данных
     */
    public void insertGames(List<GameEntity> gameEntities) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gameDao.insertAll(gameEntities);
            Log.d(TAG, "Inserted " + gameEntities.size() + " games");
        });
    }

    /**
     * Получение всех игр из базы данных
     */
    public LiveData<List<GameEntity>> getAllGames() {
        return gameDao.getAllGames();
    }

    /**
     * Получение игры по ID
     */
    public LiveData<GameEntity> getGameById(int gameId) {
        return gameDao.getGameById(gameId);
    }

    /**
     * Поиск игр по названию
     */
    public LiveData<List<GameEntity>> searchGames(String query) {
        return gameDao.searchGames(query);
    }

    /**
     * Удаление игры из базы данных
     */
    public void deleteGame(int gameId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gameDao.deleteGame(gameId);
            Log.d(TAG, "Game deleted: " + gameId);
        });
    }

    /**
     * Удаление всех игр из базы данных
     */
    public void deleteAllGames() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gameDao.deleteAllGames();
            Log.d(TAG, "All games deleted");
        });
    }

    /**
     * Получение количества сохраненных игр
     */
    public int getGameCount() {
        return gameDao.getGameCount();
    }

    /**
     * Конвертирует объекты Game из API в сущности для базы данных
     */
    public List<GameEntity> gamesToEntities(List<Game> games) {
        List<GameEntity> entities = new ArrayList<>();
        for (Game game : games) {
            entities.add(GameEntity.fromGame(game));
        }
        return entities;
    }

    /**
     * Конвертирует сущности из базы данных в объекты Game
     */
    public List<Game> entitiesToGames(List<GameEntity> entities) {
        List<Game> games = new ArrayList<>();
        for (GameEntity entity : entities) {
            games.add(entity.toGame());
        }
        return games;
    }
}