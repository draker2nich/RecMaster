package com.draker.recmaster.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.draker.recmaster.database.converter.DateConverter;

import java.util.Date;

/**
 * Entity класс для отслеживания истории просмотра фильмов
 * с отношением между пользователем и фильмом
 */
@Entity(
    tableName = "watch_history",
    foreignKeys = {
        @ForeignKey(
            entity = UserEntity.class,
            parentColumns = "username",
            childColumns = "username",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = MovieEntity.class,
            parentColumns = "id",
            childColumns = "movieId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index("username"),
        @Index("movieId")
    }
)
@TypeConverters(DateConverter.class)
public class WatchHistoryEntity {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String username;
    private int movieId;
    private Date watchedDate;
    private int userRating;
    private String userNotes;
    private int experienceGained;
    
    // Конструктор по умолчанию, обязателен для Room
    public WatchHistoryEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public Date getWatchedDate() {
        return watchedDate;
    }

    public void setWatchedDate(Date watchedDate) {
        this.watchedDate = watchedDate;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public String getUserNotes() {
        return userNotes;
    }

    public void setUserNotes(String userNotes) {
        this.userNotes = userNotes;
    }

    public int getExperienceGained() {
        return experienceGained;
    }

    public void setExperienceGained(int experienceGained) {
        this.experienceGained = experienceGained;
    }
}