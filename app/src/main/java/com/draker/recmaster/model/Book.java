package com.draker.recmaster.model;

import java.io.Serializable;
import java.util.List;

/**
 * Модель данных для представления книги из Google Books API
 */
public class Book implements Serializable {
    private String id;
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private int pageCount;
    private List<String> categories;
    private double averageRating;
    private int ratingsCount;
    private String language;
    private String previewLink;
    private String infoLink;
    private String canonicalVolumeLink;
    private VolumeImageLinks imageLinks;

    // Вложенный класс для изображений
    public static class VolumeImageLinks implements Serializable {
        private String smallThumbnail;
        private String thumbnail;
        private String small;
        private String medium;
        private String large;
        private String extraLarge;

        public String getSmallThumbnail() {
            return smallThumbnail;
        }

        public void setSmallThumbnail(String smallThumbnail) {
            this.smallThumbnail = smallThumbnail;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getExtraLarge() {
            return extraLarge;
        }

        public void setExtraLarge(String extraLarge) {
            this.extraLarge = extraLarge;
        }
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public String getCanonicalVolumeLink() {
        return canonicalVolumeLink;
    }

    public void setCanonicalVolumeLink(String canonicalVolumeLink) {
        this.canonicalVolumeLink = canonicalVolumeLink;
    }

    public VolumeImageLinks getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(VolumeImageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }

    /**
     * Получение URL обложки книги
     * @return URL обложки
     */
    public String getCoverUrl() {
        if (imageLinks != null) {
            if (imageLinks.getThumbnail() != null) {
                // Заменяем http на https для избежания смешанного контента
                return imageLinks.getThumbnail().replace("http://", "https://");
            } else if (imageLinks.getSmallThumbnail() != null) {
                return imageLinks.getSmallThumbnail().replace("http://", "https://");
            }
        }
        return null;
    }

    /**
     * Форматирует список авторов в строку
     * @return строку с авторами через запятую
     */
    public String getAuthorsString() {
        if (authors == null || authors.isEmpty()) {
            return "";
        }
        
        StringBuilder authorsString = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            if (i > 0) {
                authorsString.append(", ");
            }
            authorsString.append(authors.get(i));
        }
        return authorsString.toString();
    }

    /**
     * Форматирует список категорий в строку
     * @return строку с категориями через запятую
     */
    public String getCategoriesString() {
        if (categories == null || categories.isEmpty()) {
            return "";
        }
        
        StringBuilder categoriesString = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            if (i > 0) {
                categoriesString.append(", ");
            }
            categoriesString.append(categories.get(i));
        }
        return categoriesString.toString();
    }
    
    /**
     * Проверяет, соответствует ли книга указанным категориям
     * @param selectedCategories список выбранных категорий
     * @return true, если книга содержит хотя бы одну из указанных категорий
     */
    public boolean matchesAnyCategory(List<String> selectedCategories) {
        if (selectedCategories == null || selectedCategories.isEmpty()) {
            return true;
        }
        
        if (categories == null || categories.isEmpty()) {
            return false;
        }
        
        for (String category : categories) {
            for (String selectedCategory : selectedCategories) {
                if (category.toLowerCase().contains(selectedCategory.toLowerCase())) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", authors=" + getAuthorsString() +
                ", publisher='" + publisher + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                '}';
    }
}