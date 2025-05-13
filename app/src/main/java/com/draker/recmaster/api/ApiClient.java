package com.draker.recmaster.api;

import android.util.Log;

import com.draker.recmaster.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Клиент для доступа к различным API
 */
public class ApiClient {

    private static final String TAG = "ApiClient";
    
    // TMDB API
    private static final String TMDB_BASE_URL = BuildConfig.TMDB_BASE_URL;
    private static final String TMDB_ACCESS_TOKEN = BuildConfig.TMDB_ACCESS_TOKEN;
    private static Retrofit tmdbRetrofit = null;
    private static TmdbApi tmdbApi = null;
    private static TvShowApi tvShowApi = null;
    
    // Google Books API
    private static final String GOOGLE_BOOKS_BASE_URL = BuildConfig.GOOGLE_BOOKS_BASE_URL;
    private static Retrofit googleBooksRetrofit = null;
    private static GoogleBooksApi googleBooksApi = null;
    
    // RAWG Games API
    private static final String RAWG_BASE_URL = BuildConfig.RAWG_BASE_URL;
    private static final String RAWG_API_KEY = BuildConfig.RAWG_API_KEY;
    private static Retrofit rawgRetrofit = null;
    private static RawgGamesApi rawgGamesApi = null;

    /**
     * Получение экземпляра Retrofit для TMDB API
     */
    public static Retrofit getTmdbClient() {
        if (tmdbRetrofit == null) {
            // Настраиваем логирование для отладки
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> 
                Log.d(TAG, message)
            );
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    // Создаем перехватчик для добавления заголовка авторизации
    Interceptor authInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            
            // Проверка токена
            if (TMDB_ACCESS_TOKEN == null || TMDB_ACCESS_TOKEN.isEmpty()) {
                Log.e(TAG, "TMDB_ACCESS_TOKEN is null or empty! Check your BuildConfig");
            } else {
                Log.d(TAG, "Using TMDB_ACCESS_TOKEN: " + (TMDB_ACCESS_TOKEN.length() > 4 ? 
                        TMDB_ACCESS_TOKEN.substring(0, 4) + "..." : TMDB_ACCESS_TOKEN));
            }
            
            // Добавляем Bearer токен в заголовок
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + TMDB_ACCESS_TOKEN)
                    .header("accept", "application/json")
                    .build();
            
            return chain.proceed(newRequest);
        }
    };

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(loggingInterceptor);
            httpClient.addInterceptor(authInterceptor);

            tmdbRetrofit = new Retrofit.Builder()
                    .baseUrl(TMDB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            
            Log.d(TAG, "TMDB Retrofit client initialized with base URL: " + TMDB_BASE_URL);
        }
        return tmdbRetrofit;
    }

    /**
     * Получение API интерфейса для фильмов TMDB
     */
    public static TmdbApi getTmdbApi() {
        if (tmdbApi == null) {
            tmdbApi = getTmdbClient().create(TmdbApi.class);
            Log.d(TAG, "TMDB API interface created");
        }
        return tmdbApi;
    }
    
    /**
     * Получение API интерфейса для сериалов TMDB
     */
    public static TvShowApi getTvShowApi() {
        if (tvShowApi == null) {
            tvShowApi = getTmdbClient().create(TvShowApi.class);
            Log.d(TAG, "TMDB TV Show API interface created");
        }
        return tvShowApi;
    }
    
    /**
     * Получение экземпляра Retrofit для Google Books API
     */
    public static Retrofit getGoogleBooksClient() {
        if (googleBooksRetrofit == null) {
            // Настраиваем логирование для отладки
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> 
                Log.d(TAG, message)
            );
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(loggingInterceptor);

            googleBooksRetrofit = new Retrofit.Builder()
                    .baseUrl(GOOGLE_BOOKS_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            
            Log.d(TAG, "Google Books Retrofit client initialized with base URL: " + GOOGLE_BOOKS_BASE_URL);
        }
        return googleBooksRetrofit;
    }
    
    /**
     * Получение API интерфейса для Google Books
     */
    public static GoogleBooksApi getGoogleBooksApi() {
        if (googleBooksApi == null) {
            googleBooksApi = getGoogleBooksClient().create(GoogleBooksApi.class);
            Log.d(TAG, "Google Books API interface created");
        }
        return googleBooksApi;
    }
    
    /**
     * Получение экземпляра Retrofit для RAWG Games API
     */
    public static Retrofit getRawgGamesClient() {
        if (rawgRetrofit == null) {
            // Настраиваем логирование для отладки
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> 
                Log.d(TAG, message)
            );
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(loggingInterceptor);

            rawgRetrofit = new Retrofit.Builder()
                    .baseUrl(RAWG_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            
            Log.d(TAG, "RAWG Games Retrofit client initialized with base URL: " + RAWG_BASE_URL);
        }
        return rawgRetrofit;
    }
    
    /**
     * Получение API интерфейса для RAWG Games
     */
    public static RawgGamesApi getRawgGamesApi() {
        if (rawgGamesApi == null) {
            rawgGamesApi = getRawgGamesClient().create(RawgGamesApi.class);
            Log.d(TAG, "RAWG Games API interface created");
        }
        return rawgGamesApi;
    }
    
    /**
     * Получение API ключа для RAWG Games API
     */
    public static String getRawgApiKey() {
        return RAWG_API_KEY;
    }
}