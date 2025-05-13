package com.draker.recmaster.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Модель данных для представления игры из RAWG Video Games Database API
 */
public class Game implements Serializable {
    private int id;
    private String name;
    private String slug;
    private String released;
    private String backgroundImage;
    private double rating;
    private List<Genre> genres;
    private List<Platform> platforms;
    private List<Developer> developers;
    private List<Publisher> publishers;
    private String description;
    private int metacritic;
    private int playtime;
    private List<Store> stores;
    private String website;

    // Вложенные классы
    public static class Genre implements Serializable {
        private int id;
        private String name;
        private String slug;

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
    }

    public static class Platform implements Serializable {
        private PlatformDetails platform;

        public PlatformDetails getPlatform() {
            return platform;
        }

        public void setPlatform(PlatformDetails platform) {
            this.platform = platform;
        }

        public static class PlatformDetails implements Serializable {
            private int id;
            private String name;
            private String slug;

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
        }
    }

    public static class Developer implements Serializable {
        private int id;
        private String name;
        private String slug;

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
    }

    public static class Publisher implements Serializable {
        private int id;
        private String name;
        private String slug;

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
    }

    public static class Store implements Serializable {
        private StoreDetails store;

        public StoreDetails getStore() {
            return store;
        }

        public void setStore(StoreDetails store) {
            this.store = store;
        }

        public static class StoreDetails implements Serializable {
            private int id;
            private String name;
            private String slug;

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
        }
    }

    // Геттеры и сеттеры для основного класса
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

    /**
     * Возвращает отформатированную дату релиза в локализованном формате
     */
    public String getFormattedReleaseDate() {
        if (released == null || released.isEmpty()) {
            return "";
        }
        
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
            Date date = inputFormat.parse(released);
            if (date != null) {
                return outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return released;
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

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }

    public List<Publisher> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<Publisher> publishers) {
        this.publishers = publishers;
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

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Возвращает строковое представление жанров игры
     */
    public String getGenresString() {
        if (genres == null || genres.isEmpty()) {
            return "";
        }
        
        StringBuilder genresStr = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            if (i > 0) {
                genresStr.append(", ");
            }
            genresStr.append(genres.get(i).getName());
        }
        
        return genresStr.toString();
    }

    /**
     * Возвращает строковое представление платформ игры
     */
    public String getPlatformsString() {
        if (platforms == null || platforms.isEmpty()) {
            return "";
        }
        
        StringBuilder platformsStr = new StringBuilder();
        for (int i = 0; i < platforms.size(); i++) {
            if (i > 0) {
                platformsStr.append(", ");
            }
            platformsStr.append(platforms.get(i).getPlatform().getName());
        }
        
        return platformsStr.toString();
    }

    /**
     * Возвращает строковое представление разработчиков игры
     */
    public String getDevelopersString() {
        if (developers == null || developers.isEmpty()) {
            return "";
        }
        
        StringBuilder developersStr = new StringBuilder();
        for (int i = 0; i < developers.size(); i++) {
            if (i > 0) {
                developersStr.append(", ");
            }
            developersStr.append(developers.get(i).getName());
        }
        
        return developersStr.toString();
    }

    /**
     * Проверяет, содержит ли игра указанный жанр
     */
    public boolean hasGenre(String genreName) {
        if (genres == null || genres.isEmpty() || genreName == null || genreName.isEmpty()) {
            return false;
        }
        
        for (Genre genre : genres) {
            if (genre.getName().equalsIgnoreCase(genreName)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Проверяет, доступна ли игра на указанной платформе
     */
    public boolean isOnPlatform(String platformName) {
        if (platforms == null || platforms.isEmpty() || platformName == null || platformName.isEmpty()) {
            return false;
        }
        
        for (Platform platform : platforms) {
            if (platform.getPlatform().getName().equalsIgnoreCase(platformName)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", released='" + released + '\'' +
                ", rating=" + rating +
                ", metacritic=" + metacritic +
                '}';
    }
}