package com.draker.recmaster.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Класс для десериализации ответа от Google Books API
 */
public class BookResponse {
    private String kind;
    private int totalItems;
    private List<BookItem> items;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<BookItem> getItems() {
        return items;
    }

    public void setItems(List<BookItem> items) {
        this.items = items;
    }

    /**
     * Внутренний класс для представления элемента книги в ответе API
     */
    public static class BookItem {
        private String kind;
        private String id;
        private String etag;
        private String selfLink;
        @SerializedName("volumeInfo")
        private Book volumeInfo;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEtag() {
            return etag;
        }

        public void setEtag(String etag) {
            this.etag = etag;
        }

        public String getSelfLink() {
            return selfLink;
        }

        public void setSelfLink(String selfLink) {
            this.selfLink = selfLink;
        }

        public Book getVolumeInfo() {
            return volumeInfo;
        }

        public void setVolumeInfo(Book volumeInfo) {
            this.volumeInfo = volumeInfo;
        }
    }
}