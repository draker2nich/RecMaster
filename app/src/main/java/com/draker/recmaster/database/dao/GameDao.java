package com.draker.recmaster.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.draker.recmaster.database.entity.GameEntity;

import java.util.List;

/**
 * Data Access Object для работы с играми в базе данных
 */
@Dao
public interface GameDao {

    /**
     * Вставка игры в базу данных
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GameEntity gameEntity);

    /**
     * Вставка списка игр в базу данных
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GameEntity> gameEntities);

    /**
     * Получение всех игр из базы данных
     */
    @Query("SELECT * FROM games ORDER BY savedTimestamp DESC")
    LiveData<List<GameEntity>> getAllGames();

    /**
     * Получение игры по ID
     */
    @Query("SELECT * FROM games WHERE id = :gameId")
    LiveData<GameEntity> getGameById(int gameId);

    /**
     * Поиск игр по названию
     */
    @Query("SELECT * FROM games WHERE name LIKE '%' || :query || '%' ORDER BY rating DESC")
    LiveData<List<GameEntity>> searchGames(String query);

    /**
     * Удаление игры из базы данных
     */
    @Query("DELETE FROM games WHERE id = :gameId")
    void deleteGame(int gameId);

    /**
     * Удаление всех игр из базы данных
     */
    @Query("DELETE FROM games")
    void deleteAllGames();

    /**
     * Получение количества сохраненных игр
     */
    @Query("SELECT COUNT(*) FROM games")
    int getGameCount();
}