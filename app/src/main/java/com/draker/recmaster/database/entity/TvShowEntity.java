package com.draker.recmaster.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.draker.recmaster.database.converter.GenreIdsConverter;
import com.draker.recmaster.model.TvShow;

import java.util.List;

/**
 * Сущность для хранения данных о сериалах в базе данных Room
 */
@Entity(tableName = "tv_shows")
@TypeConverters(GenreIdsConverter.class)
public class TvShowEntity {

    @PrimaryKey
    private int id;
    private String name;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private float voteAverage;
    private int voteCount;
    private String firstAirDate;
    private List<Integer> genreIds;
    private float popularity;
    private int numberOfSeasons;
    private int numberOfEpisodes;
    private String status;
    private long savedTimestamp;

    /**
     * Конвертирует объект TvShow из API в сущность для базы данных
     */
    public static TvShowEntity fromTvShow(TvShow tvShow) {
        TvShowEntity entity = new TvShowEntity();
        entity.setId(tvShow.getId());
        entity.setName(tvShow.getName());
        entity.setOverview(tvShow.getOverview());
        entity.setPosterPath(tvShow.getPosterPath());
        entity.setBackdropPath(tvShow.getBackdropPath());
        entity.setVoteAverage(tvShow.getVoteAverage());
        entity.setVoteCount(tvShow.getVoteCount());
        entity.setFirstAirDate(tvShow.getFirstAirDate());
        entity.setGenreIds(tvShow.getGenreIds());
        entity.setPopularity(tvShow.getPopularity());
        entity.setNumberOfSeasons(tvShow.getNumberOfSeasons());
        entity.setNumberOfEpisodes(tvShow.getNumberOfEpisodes());
        entity.setStatus(tvShow.getStatus());
        entity.setSavedTimestamp(System.currentTimeMillis());
        return entity;
    }

    /**
     * Конвертирует сущность из базы данных в объект TvShow
     */
    public TvShow toTvShow() {
        TvShow tvShow = new TvShow();
        tvShow.setId(id);
        tvShow.setName(name);
        tvShow.setOverview(overview);
        tvShow.setPosterPath(posterPath);
        tvShow.setBackdropPath(backdropPath);
        tvShow.setVoteAverage(voteAverage);
        tvShow.setVoteCount(voteCount);
        tvShow.setFirstAirDate(firstAirDate);
        tvShow.setGenreIds(genreIds);
        tvShow.setPopularity(popularity);
        tvShow.setNumberOfSeasons(numberOfSeasons);
        tvShow.setNumberOfEpisodes(numberOfEpisodes);
        tvShow.setStatus(status);
        return tvShow;
    }

    // Геттеры и сеттеры

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
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

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getSavedTimestamp() {
        return savedTimestamp;
    }

    public void setSavedTimestamp(long savedTimestamp) {
        this.savedTimestamp = savedTimestamp;
    }
}