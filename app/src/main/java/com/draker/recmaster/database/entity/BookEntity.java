package com.draker.recmaster.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.draker.recmaster.database.converter.StringListConverter;
import com.draker.recmaster.model.Book;
import com.google.gson.Gson;

import java.util.List;

/**
 * Сущность для хранения данных о книгах в базе данных Room
 */
@Entity(tableName = "books")
@TypeConverters(StringListConverter.class)
public class BookEntity {

    @PrimaryKey
    @NonNull
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
    private String imageLinksJson; // Хранит JSON представление VolumeImageLinks
    private long savedTimestamp;

    /**
     * Конвертирует объект Book из API в сущность для базы данных
     */
    public static BookEntity fromBook(Book book) {
        BookEntity entity = new BookEntity();
        entity.setId(book.getId());
        entity.setTitle(book.getTitle());
        entity.setAuthors(book.getAuthors());
        entity.setPublisher(book.getPublisher());
        entity.setPublishedDate(book.getPublishedDate());
        entity.setDescription(book.getDescription());
        entity.setPageCount(book.getPageCount());
        entity.setCategories(book.getCategories());
        entity.setAverageRating(book.getAverageRating());
        entity.setRatingsCount(book.getRatingsCount());
        entity.setLanguage(book.getLanguage());
        entity.setPreviewLink(book.getPreviewLink());
        entity.setInfoLink(book.getInfoLink());
        entity.setCanonicalVolumeLink(book.getCanonicalVolumeLink());
        
        // Преобразуем объект VolumeImageLinks в JSON для хранения
        if (book.getImageLinks() != null) {
            Gson gson = new Gson();
            entity.setImageLinksJson(gson.toJson(book.getImageLinks()));
        }
        
        entity.setSavedTimestamp(System.currentTimeMillis());
        return entity;
    }

    /**
     * Конвертирует сущность из базы данных в объект Book
     */
    public Book toBook() {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthors(authors);
        book.setPublisher(publisher);
        book.setPublishedDate(publishedDate);
        book.setDescription(description);
        book.setPageCount(pageCount);
        book.setCategories(categories);
        book.setAverageRating(averageRating);
        book.setRatingsCount(ratingsCount);
        book.setLanguage(language);
        book.setPreviewLink(previewLink);
        book.setInfoLink(infoLink);
        book.setCanonicalVolumeLink(canonicalVolumeLink);
        
        // Преобразуем JSON обратно в объект VolumeImageLinks
        if (imageLinksJson != null && !imageLinksJson.isEmpty()) {
            Gson gson = new Gson();
            Book.VolumeImageLinks imageLinks = gson.fromJson(imageLinksJson, Book.VolumeImageLinks.class);
            book.setImageLinks(imageLinks);
        }
        
        return book;
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

    public String getImageLinksJson() {
        return imageLinksJson;
    }

    public void setImageLinksJson(String imageLinksJson) {
        this.imageLinksJson = imageLinksJson;
    }

    public long getSavedTimestamp() {
        return savedTimestamp;
    }

    public void setSavedTimestamp(long savedTimestamp) {
        this.savedTimestamp = savedTimestamp;
    }
}