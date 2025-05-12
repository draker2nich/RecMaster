package com.draker.recmaster.api;

import com.draker.recmaster.model.GenreResponse;
import com.draker.recmaster.model.Movie;
import com.draker.recmaster.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Интерфейс для Retrofit с эндпоинтами TMDB API
 * Используется Bearer авторизация через заголовок, поэтому api_key не нужен в параметрах
 */
public interface TmdbApi {

    /**
     * Получение списка популярных фильмов
     */
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("language") String language,
            @Query("page") int page
    );

    /**
     * Получение списка фильмов с высоким рейтингом
     */
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(
            @Query("language") String language,
            @Query("page") int page
    );

    /**
     * Получение списка недавно вышедших фильмов
     */
    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(
            @Query("language") String language,
            @Query("page") int page
    );

    /**
     * Получение списка предстоящих фильмов
     */
    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(
            @Query("language") String language,
            @Query("page") int page
    );

    /**
     * Получение детальной информации о фильме
     */
    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(
            @Path("movie_id") int movieId,
            @Query("language") String language
    );

    /**
     * Получение списка жанров фильмов
     */
    @GET("genre/movie/list")
    Call<GenreResponse> getMovieGenres(
            @Query("language") String language
    );

    /**
     * Поиск фильмов по запросу
     */
    @GET("search/movie")
    Call<MovieResponse> searchMovies(
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page,
            @Query("include_adult") boolean includeAdult
    );
}