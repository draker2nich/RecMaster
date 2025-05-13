package com.draker.recmaster.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.draker.recmaster.database.entity.MovieEntity;

import java.util.List;

/**
 * Data Access Object для операций с таблицей фильмов
 */
@Dao
public interface MovieDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(MovieEntity movie);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MovieEntity> movies);
    
    @Update
    void update(MovieEntity movie);
    
    @Delete
    void delete(MovieEntity movie);
    
    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<MovieEntity> getMovieById(int id);
    
    @Query("SELECT * FROM movies WHERE id = :id")
    MovieEntity getMovieByIdSync(int id);
    
    @Query("SELECT * FROM movies WHERE watched = 1 ORDER BY watchedDate DESC")
    LiveData<List<MovieEntity>> getWatchedMovies();
    
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'")
    LiveData<List<MovieEntity>> searchMovies(String query);
    
    @Query("UPDATE movies SET watched = :watched, watchedDate = :watchedDate, userRating = :userRating, userNotes = :userNotes WHERE id = :id")
    void updateWatchedStatus(int id, boolean watched, long watchedDate, int userRating, String userNotes);
    
    @Query("DELETE FROM movies")
    void deleteAll();
}