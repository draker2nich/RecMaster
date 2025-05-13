package com.draker.recmaster.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.draker.recmaster.database.entity.WatchHistoryEntity;

import java.util.List;

/**
 * Data Access Object для операций с таблицей истории просмотров
 */
@Dao
public interface WatchHistoryDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(WatchHistoryEntity watchHistory);
    
    @Update
    void update(WatchHistoryEntity watchHistory);
    
    @Delete
    void delete(WatchHistoryEntity watchHistory);
    
    @Query("SELECT * FROM watch_history WHERE id = :id")
    LiveData<WatchHistoryEntity> getWatchHistoryById(long id);
    
    @Query("SELECT * FROM watch_history WHERE username = :username ORDER BY watchedDate DESC")
    LiveData<List<WatchHistoryEntity>> getWatchHistoryByUsername(String username);
    
    @Query("SELECT * FROM watch_history WHERE movieId = :movieId AND username = :username")
    WatchHistoryEntity getWatchHistoryByMovieAndUsername(int movieId, String username);
    
    @Query("DELETE FROM watch_history WHERE username = :username")
    void deleteAllForUser(String username);
    
    @Query("DELETE FROM watch_history")
    void deleteAll();
}