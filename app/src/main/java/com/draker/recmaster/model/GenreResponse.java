package com.draker.recmaster.model;

import java.util.List;

/**
 * Класс для десериализации ответа от API со списком жанров
 */
public class GenreResponse {

    private List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}