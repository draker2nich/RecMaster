package com.draker.recmaster.api;

import com.draker.recmaster.model.GenreResponse;
import com.draker.recmaster.model.TvShow;
import com.draker.recmaster.model.TvShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Интерфейс для Retrofit с эндпоинтами TMDB API для сериалов
 * Используется Bearer авторизация через заголовок, поэтому api_key не нужен в параметрах
 */
public interface TvShowApi {

    /**
     * Получение списка популярных сериалов
     */
    @GET("tv/popular")
    Call<TvShowResponse> getPopularTvShows(
            @Query("language") String language,
            @Query("page") int page
    );

    /**
     * Получение списка сериалов с высоким рейтингом
     */
    @GET("tv/top_rated")
    Call<TvShowResponse> getTopRatedTvShows(
            @Query("language") String language,
            @Query("page") int page
    );

    /**
     * Получение списка сериалов, которые сейчас в эфире
     */
    @GET("tv/on_the_air")
    Call<TvShowResponse> getOnTheAirTvShows(
            @Query("language") String language,
            @Query("page") int page
    );

    /**
     * Получение списка сериалов, которые выходят сегодня
     */
    @GET("tv/airing_today")
    Call<TvShowResponse> getAiringTodayTvShows(
            @Query("language") String language,
            @Query("page") int page
    );

    /**
     * Получение детальной информации о сериале
     */
    @GET("tv/{tv_id}")
    Call<TvShow> getTvShowDetails(
            @Path("tv_id") int tvId,
            @Query("language") String language
    );

    /**
     * Получение списка жанров сериалов
     */
    @GET("genre/tv/list")
    Call<GenreResponse> getTvShowGenres(
            @Query("language") String language
    );

    /**
     * Поиск сериалов по запросу
     */
    @GET("search/tv")
    Call<TvShowResponse> searchTvShows(
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page,
            @Query("include_adult") boolean includeAdult
    );
}