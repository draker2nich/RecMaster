package com.draker.recmaster.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.draker.recmaster.database.entity.TvShowEntity;

import java.util.List;

/**
 * Data Access Object для работы с сериалами в базе данных
 */
@Dao
public interface TvShowDao {

    /**
     * Вставка сериала в базу данных
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TvShowEntity tvShowEntity);

    /**
     * Вставка списка сериалов в базу данных
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TvShowEntity> tvShowEntities);

    /**
     * Получение всех сериалов из базы данных
     */
    @Query("SELECT * FROM tv_shows ORDER BY savedTimestamp DESC")
    LiveData<List<TvShowEntity>> getAllTvShows();

    /**
     * Получение сериала по ID
     */
    @Query("SELECT * FROM tv_shows WHERE id = :tvShowId")
    LiveData<TvShowEntity> getTvShowById(int tvShowId);

    /**
     * Поиск сериалов по названию
     */
    @Query("SELECT * FROM tv_shows WHERE name LIKE '%' || :query || '%' ORDER BY popularity DESC")
    LiveData<List<TvShowEntity>> searchTvShows(String query);

    /**
     * Удаление сериала из базы данных
     */
    @Query("DELETE FROM tv_shows WHERE id = :tvShowId")
    void deleteTvShow(int tvShowId);

    /**
     * Удаление всех сериалов из базы данных
     */
    @Query("DELETE FROM tv_shows")
    void deleteAllTvShows();

    /**
     * Получение количества сохраненных сериалов
     */
    @Query("SELECT COUNT(*) FROM tv_shows")
    int getTvShowCount();
}