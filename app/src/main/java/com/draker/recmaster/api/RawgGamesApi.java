package com.draker.recmaster.api;

import com.draker.recmaster.model.Game;
import com.draker.recmaster.model.GameResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Интерфейс для Retrofit с эндпоинтами RAWG Video Games Database API
 */
public interface RawgGamesApi {

    /**
     * Получение списка популярных игр
     */
    @GET("games")
    Call<GameResponse> getGames(
            @Query("key") String apiKey,
            @Query("page") int page,
            @Query("page_size") int pageSize,
            @Query("ordering") String ordering
    );

    /**
     * Получение детальной информации об игре
     */
    @GET("games/{id}")
    Call<Game> getGameDetails(
            @Path("id") int gameId,
            @Query("key") String apiKey
    );

    /**
     * Поиск игр по запросу
     */
    @GET("games")
    Call<GameResponse> searchGames(
            @Query("key") String apiKey,
            @Query("search") String searchQuery,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );

    /**
     * Получение игр по жанру
     */
    @GET("games")
    Call<GameResponse> getGamesByGenre(
            @Query("key") String apiKey,
            @Query("genres") String genre,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );

    /**
     * Получение игр по платформе
     */
    @GET("games")
    Call<GameResponse> getGamesByPlatform(
            @Query("key") String apiKey,
            @Query("platforms") String platform,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );

    /**
     * Получение предстоящих игр
     */
    @GET("games")
    Call<GameResponse> getUpcomingGames(
            @Query("key") String apiKey,
            @Query("dates") String dateRange,
            @Query("ordering") String ordering,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );
}