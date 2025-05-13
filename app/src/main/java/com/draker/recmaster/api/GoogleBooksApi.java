package com.draker.recmaster.api;

import com.draker.recmaster.model.Book;
import com.draker.recmaster.model.BookResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Интерфейс для Retrofit с эндпоинтами Google Books API
 * API не требует ключа для большинства запросов
 */
public interface GoogleBooksApi {

    /**
     * Поиск книг по запросу
     */
    @GET("volumes")
    Call<BookResponse> searchBooks(
            @Query("q") String query,
            @Query("startIndex") int startIndex,
            @Query("maxResults") int maxResults,
            @Query("langRestrict") String languageRestriction
    );

    /**
     * Поиск книг по категории
     */
    @GET("volumes")
    Call<BookResponse> getBooksByCategory(
            @Query("q") String query,
            @Query("startIndex") int startIndex,
            @Query("maxResults") int maxResults,
            @Query("langRestrict") String languageRestriction
    );

    /**
     * Получение детальной информации о конкретной книге
     */
    @GET("volumes/{volumeId}")
    Call<Book> getBookDetails(
            @Path("volumeId") String volumeId
    );

    /**
     * Поиск книг по автору
     */
    @GET("volumes")
    Call<BookResponse> getBooksByAuthor(
            @Query("q") String authorQuery,
            @Query("startIndex") int startIndex,
            @Query("maxResults") int maxResults,
            @Query("langRestrict") String languageRestriction
    );

    /**
     * Поиск книг по издателю
     */
    @GET("volumes")
    Call<BookResponse> getBooksByPublisher(
            @Query("q") String publisherQuery,
            @Query("startIndex") int startIndex,
            @Query("maxResults") int maxResults,
            @Query("langRestrict") String languageRestriction
    );

    /**
     * Получение новинок книг
     */
    @GET("volumes")
    Call<BookResponse> getNewReleases(
            @Query("q") String query,
            @Query("orderBy") String orderBy,
            @Query("startIndex") int startIndex,
            @Query("maxResults") int maxResults,
            @Query("langRestrict") String languageRestriction
    );
}