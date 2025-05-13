package com.draker.recmaster.service;

import android.util.Log;

import com.draker.recmaster.model.Book;
import com.draker.recmaster.model.Game;
import com.draker.recmaster.model.Movie;
import com.draker.recmaster.model.RecommendationFilter;
import com.draker.recmaster.model.TvShow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Сервис для генерации рекомендаций контента (фильмы, сериалы, книги, игры)
 */
public class ContentRecommendationService {
    
    private static final String TAG = "ContentRecommendationService";
    private static ContentRecommendationService instance;
    
    private ContentRecommendationService() {
        Log.d(TAG, "ContentRecommendationService initialized");
    }
    
    public static synchronized ContentRecommendationService getInstance() {
        if (instance == null) {
            instance = new ContentRecommendationService();
        }
        return instance;
    }
    
    /**
     * Создает рекомендации фильмов на основе фильтров
     * 
     * @param movies список всех доступных фильмов
     * @param filter фильтр для рекомендаций
     * @return отфильтрованный список рекомендованных фильмов
     */
    public List<Movie> getMovieRecommendations(List<Movie> movies, RecommendationFilter filter) {
        if (movies == null || movies.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Movie> filteredMovies = new ArrayList<>(movies);
        
        // Применяем фильтры
        if (filter != null) {
            // Фильтрация по жанрам
            if (!filter.getSelectedGenreIds().isEmpty()) {
                filteredMovies = filteredMovies.stream()
                        .filter(movie -> movie.matchesAnyGenre(filter.getSelectedGenreIds()))
                        .collect(Collectors.toList());
            }
            
            // Фильтрация по настроению
            if (filter.getMood() != null && !filter.getMood().isEmpty()) {
                filteredMovies = filteredMovies.stream()
                        .filter(movie -> movie.matchesMood(filter.getMood()))
                        .collect(Collectors.toList());
            }
            
            // Фильтрация по продолжительности
            if (filter.getMaxDuration() > 0) {
                filteredMovies = filteredMovies.stream()
                        .filter(movie -> movie.matchesDuration(filter.getMaxDuration()))
                        .collect(Collectors.toList());
            }
        }
        
        // Перемешиваем результаты для разнообразия
        Collections.shuffle(filteredMovies, new Random(System.currentTimeMillis()));
        
        return filteredMovies;
    }
    
    /**
     * Создает рекомендации сериалов на основе фильтров
     * 
     * @param tvShows список всех доступных сериалов
     * @param filter фильтр для рекомендаций
     * @return отфильтрованный список рекомендованных сериалов
     */
    public List<TvShow> getTvShowRecommendations(List<TvShow> tvShows, RecommendationFilter filter) {
        if (tvShows == null || tvShows.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<TvShow> filteredTvShows = new ArrayList<>(tvShows);
        
        // Применяем фильтры
        if (filter != null) {
            // Фильтрация по жанрам
            if (!filter.getSelectedGenreIds().isEmpty()) {
                filteredTvShows = filteredTvShows.stream()
                        .filter(tvShow -> tvShow.matchesAnyGenre(filter.getSelectedGenreIds()))
                        .collect(Collectors.toList());
            }
            
            // Фильтрация по настроению
            if (filter.getMood() != null && !filter.getMood().isEmpty()) {
                filteredTvShows = filteredTvShows.stream()
                        .filter(tvShow -> tvShow.matchesMood(filter.getMood()))
                        .collect(Collectors.toList());
            }
        }
        
        // Перемешиваем результаты для разнообразия
        Collections.shuffle(filteredTvShows, new Random(System.currentTimeMillis()));
        
        return filteredTvShows;
    }
    
    /**
     * Создает рекомендации книг на основе фильтров
     * 
     * @param books список всех доступных книг
     * @param filter фильтр для рекомендаций
     * @return отфильтрованный список рекомендованных книг
     */
    public List<Book> getBookRecommendations(List<Book> books, List<String> selectedCategories) {
        if (books == null || books.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Book> filteredBooks = new ArrayList<>(books);
        
        // Применяем фильтры
        if (selectedCategories != null && !selectedCategories.isEmpty()) {
            // Фильтрация по категориям
            filteredBooks = filteredBooks.stream()
                    .filter(book -> book.matchesAnyCategory(selectedCategories))
                    .collect(Collectors.toList());
        }
        
        // Перемешиваем результаты для разнообразия
        Collections.shuffle(filteredBooks, new Random(System.currentTimeMillis()));
        
        return filteredBooks;
    }
    
    /**
     * Создает рекомендации игр на основе фильтров
     * 
     * @param games список всех доступных игр
     * @param genreNames список выбранных жанров
     * @param platformNames список выбранных платформ
     * @return отфильтрованный список рекомендованных игр
     */
    public List<Game> getGameRecommendations(List<Game> games, List<String> genreNames, List<String> platformNames) {
        if (games == null || games.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Game> filteredGames = new ArrayList<>(games);
        
        // Применяем фильтр по жанрам
        if (genreNames != null && !genreNames.isEmpty()) {
            List<Game> gamesByGenre = new ArrayList<>();
            for (Game game : filteredGames) {
                for (String genreName : genreNames) {
                    if (game.hasGenre(genreName)) {
                        gamesByGenre.add(game);
                        break;
                    }
                }
            }
            filteredGames = gamesByGenre;
        }
        
        // Применяем фильтр по платформам
        if (platformNames != null && !platformNames.isEmpty()) {
            List<Game> gamesByPlatform = new ArrayList<>();
            for (Game game : filteredGames) {
                for (String platformName : platformNames) {
                    if (game.isOnPlatform(platformName)) {
                        gamesByPlatform.add(game);
                        break;
                    }
                }
            }
            filteredGames = gamesByPlatform;
        }
        
        // Перемешиваем результаты для разнообразия
        Collections.shuffle(filteredGames, new Random(System.currentTimeMillis()));
        
        return filteredGames;
    }
}