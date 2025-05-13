package com.draker.recmaster.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.draker.recmaster.database.converter.GameDataConverter;
import com.draker.recmaster.model.Game;
import com.google.gson.Gson;

import java.util.List;

/**
 * Сущность для хранения данных об играх в базе данных Room
 */
@Entity(tableName = "games")
@TypeConverters(GameDataConverter.class)
public class GameEntity {

    @PrimaryKey
    private int id;
    private String name;
    private String slug;
    private String released;
    private String backgroundImage;
    private double rating;
    private String genresJson; // Хранит JSON представление списка жанров
    private String platformsJson; // Хранит JSON представление списка платформ
    private String developersJson; // Хранит JSON представление списка разработчиков
    private String publishersJson; // Хранит JSON представление списка издателей
    private String description;
    private int metacritic;
    private int playtime;
    private String storesJson; // Хранит JSON представление списка магазинов
    private String website;
    private long savedTimestamp;

    /**
     * Конвертирует объект Game из API в сущность для базы данных
     */
    public static GameEntity fromGame(Game game) {
        Gson gson = new Gson();
        GameEntity entity = new GameEntity();
        entity.setId(game.getId());
        entity.setName(game.getName());
        entity.setSlug(game.getSlug());
        entity.setReleased(game.getReleased());
        entity.setBackgroundImage(game.getBackgroundImage());
        entity.setRating(game.getRating());
        
        // Преобразуем списки объектов в JSON для хранения
        if (game.getGenres() != null) {
            entity.setGenresJson(gson.toJson(game.getGenres()));
        }
        if (game.getPlatforms() != null) {
            entity.setPlatformsJson(gson.toJson(game.getPlatforms()));
        }
        if (game.getDevelopers() != null) {
            entity.setDevelopersJson(gson.toJson(game.getDevelopers()));
        }
        if (game.getPublishers() != null) {
            entity.setPublishersJson(gson.toJson(game.getPublishers()));
        }
        
        entity.setDescription(game.getDescription());
        entity.setMetacritic(game.getMetacritic());
        entity.setPlaytime(game.getPlaytime());
        
        if (game.getStores() != null) {
            entity.setStoresJson(gson.toJson(game.getStores()));
        }
        
        entity.setWebsite(game.getWebsite());
        entity.setSavedTimestamp(System.currentTimeMillis());
        return entity;
    }

    /**
     * Конвертирует сущность из базы данных в объект Game
     */
    public Game toGame() {
        Gson gson = new Gson();
        Game game = new Game();
        game.setId(id);
        game.setName(name);
        game.setSlug(slug);
        game.setReleased(released);
        game.setBackgroundImage(backgroundImage);
        game.setRating(rating);
        
        // Преобразуем JSON обратно в объекты
        if (genresJson != null && !genresJson.isEmpty()) {
            List<Game.Genre> genres = gson.fromJson(genresJson, 
                    GameDataConverter.getGenreListType());
            game.setGenres(genres);
        }
        
        if (platformsJson != null && !platformsJson.isEmpty()) {
            List<Game.Platform> platforms = gson.fromJson(platformsJson, 
                    GameDataConverter.getPlatformListType());
            game.setPlatforms(platforms);
        }
        
        if (developersJson != null && !developersJson.isEmpty()) {
            List<Game.Developer> developers = gson.fromJson(developersJson, 
                    GameDataConverter.getDeveloperListType());
            game.setDevelopers(developers);
        }
        
        if (publishersJson != null && !publishersJson.isEmpty()) {
            List<Game.Publisher> publishers = gson.fromJson(publishersJson, 
                    GameDataConverter.getPublisherListType());
            game.setPublishers(publishers);
        }
        
        game.setDescription(description);
        game.setMetacritic(metacritic);
        game.setPlaytime(playtime);
        
        if (storesJson != null && !storesJson.isEmpty()) {
            List<Game.Store> stores = gson.fromJson(storesJson, 
                    GameDataConverter.getStoreListType());
            game.setStores(stores);
        }
        
        game.setWebsite(website);
        return game;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getGenresJson() {
        return genresJson;
    }

    public void setGenresJson(String genresJson) {
        this.genresJson = genresJson;
    }

    public String getPlatformsJson() {
        return platformsJson;
    }

    public void setPlatformsJson(String platformsJson) {
        this.platformsJson = platformsJson;
    }

    public String getDevelopersJson() {
        return developersJson;
    }

    public void setDevelopersJson(String developersJson) {
        this.developersJson = developersJson;
    }

    public String getPublishersJson() {
        return publishersJson;
    }

    public void setPublishersJson(String publishersJson) {
        this.publishersJson = publishersJson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(int metacritic) {
        this.metacritic = metacritic;
    }

    public int getPlaytime() {
        return playtime;
    }

    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }

    public String getStoresJson() {
        return storesJson;
    }

    public void setStoresJson(String storesJson) {
        this.storesJson = storesJson;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public long getSavedTimestamp() {
        return savedTimestamp;
    }

    public void setSavedTimestamp(long savedTimestamp) {
        this.savedTimestamp = savedTimestamp;
    }
}