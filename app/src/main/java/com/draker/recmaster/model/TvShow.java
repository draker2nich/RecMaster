package com.draker.recmaster.model;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Модель данных для представления сериала из TMDB API
 */
public class TvShow implements Serializable {
    private static final String TAG = "TvShow";

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
    private List<String> networks;

    // Базовый URL для изображений
    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342";
    private static final String BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780";

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

    // Получение полного URL для постера
    public String getPosterUrl() {
        if (posterPath != null && !posterPath.isEmpty()) {
            return POSTER_BASE_URL + posterPath;
        }
        return null;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    // Получение полного URL для фона
    public String getBackdropUrl() {
        if (backdropPath != null && !backdropPath.isEmpty()) {
            return BACKDROP_BASE_URL + backdropPath;
        }
        return null;
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

    /**
     * Возвращает отформатированную дату выхода в локализованном формате
     */
    public String getFormattedFirstAirDate() {
        if (firstAirDate == null || firstAirDate.isEmpty()) {
            return "";
        }
        
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
            Date date = inputFormat.parse(firstAirDate);
            if (date != null) {
                return outputFormat.format(date);
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + firstAirDate, e);
        }
        
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

    public List<String> getNetworks() {
        return networks;
    }

    public void setNetworks(List<String> networks) {
        this.networks = networks;
    }

    /**
     * Получает строковое представление жанров сериала
     */
    public String getGenresString(Map<Integer, String> genreMap) {
        if (genreIds == null || genreIds.isEmpty() || genreMap == null || genreMap.isEmpty()) {
            return "";
        }
        
        StringBuilder genres = new StringBuilder();
        for (Integer genreId : genreIds) {
            String genreName = genreMap.get(genreId);
            if (genreName != null) {
                if (genres.length() > 0) {
                    genres.append(", ");
                }
                genres.append(genreName);
            }
        }
        
        return genres.toString();
    }
    
    /**
     * Форматированное отображение информации о сезонах
     */
    public String getFormattedSeasonInfo() {
        if (numberOfSeasons <= 0) {
            return "";
        }
        
        String seasonText = numberOfSeasons + " " + 
                (numberOfSeasons == 1 ? "сезон" : 
                 (numberOfSeasons > 1 && numberOfSeasons < 5) ? "сезона" : "сезонов");
                 
        if (numberOfEpisodes > 0) {
            seasonText += ", " + numberOfEpisodes + " " + 
                    (numberOfEpisodes == 1 ? "эпизод" : 
                     (numberOfEpisodes > 1 && numberOfEpisodes < 5) ? "эпизода" : "эпизодов");
        }
        
        return seasonText;
    }
    
    /**
     * Определяет, соответствует ли сериал определенному настроению
     */
    public boolean matchesMood(String mood) {
        if (mood == null || mood.isEmpty()) {
            return true;
        }
        
        if (genreIds == null || genreIds.isEmpty()) {
            return false;
        }
        
        // Примерное соответствие жанров настроениям
        if (mood.equalsIgnoreCase("happy")) {
            return genreIds.contains(35) || genreIds.contains(10751); // Comedy, Family
        } else if (mood.equalsIgnoreCase("sad")) {
            return genreIds.contains(18); // Drama 
        } else if (mood.equalsIgnoreCase("excited")) {
            return genreIds.contains(28) || genreIds.contains(12) || genreIds.contains(10759); // Action, Adventure, Action & Adventure
        } else if (mood.equalsIgnoreCase("relaxed")) {
            return genreIds.contains(99) || genreIds.contains(36); // Documentary, History
        } else {
            return true;
        }
    }
    
    /**
     * Проверяет, содержит ли сериал указанный жанр
     * @param genreId ID жанра
     * @return true, если сериал принадлежит указанному жанру
     */
    public boolean hasGenre(int genreId) {
        return genreIds != null && genreIds.contains(genreId);
    }
    
    /**
     * Проверяет, соответствует ли сериал всем указанным жанрам
     * @param selectedGenreIds список ID выбранных жанров
     * @return true, если сериал содержит все указанные жанры
     */
    public boolean matchesAllGenres(List<Integer> selectedGenreIds) {
        if (selectedGenreIds == null || selectedGenreIds.isEmpty()) {
            return true;
        }
        
        if (genreIds == null || genreIds.isEmpty()) {
            return false;
        }
        
        return genreIds.containsAll(selectedGenreIds);
    }
    
    /**
     * Проверяет, соответствует ли сериал хотя бы одному из указанных жанров
     * @param selectedGenreIds список ID выбранных жанров
     * @return true, если сериал содержит хотя бы один из указанных жанров
     */
    public boolean matchesAnyGenre(List<Integer> selectedGenreIds) {
        if (selectedGenreIds == null || selectedGenreIds.isEmpty()) {
            return true;
        }
        
        if (genreIds == null || genreIds.isEmpty()) {
            return false;
        }
        
        for (Integer genreId : selectedGenreIds) {
            if (genreIds.contains(genreId)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public String toString() {
        return "TvShow{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstAirDate='" + firstAirDate + '\'' +
                ", voteAverage=" + voteAverage +
                ", numberOfSeasons=" + numberOfSeasons +
                '}';
    }
}