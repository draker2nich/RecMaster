package com.draker.recmaster.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.draker.recmaster.database.converter.DateConverter;
import com.draker.recmaster.database.converter.GenreIdsConverter;

import java.util.Date;
import java.util.List;

/**
 * Entity класс, представляющий фильм в базе данных Room
 */
@Entity(tableName = "movies")
@TypeConverters({GenreIdsConverter.class, DateConverter.class})
public class MovieEntity {
    
    @PrimaryKey
    private int id;
    
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private float voteAverage;
    private int voteCount;
    private String releaseDate;
    private List<Integer> genreIds;
    private float popularity;
    private boolean adult;
    
    // Дополнительные поля для отслеживания в приложении
    private boolean watched; // Просмотрен ли фильм
    private Date watchedDate; // Дата просмотра
    private int userRating; // Рейтинг пользователя (от 1 до 5)
    private String userNotes; // Заметки пользователя
    
    // Конструктор по умолчанию, обязателен для Room
    public MovieEntity() {
    }
    
    // Сеттеры и геттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
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
}