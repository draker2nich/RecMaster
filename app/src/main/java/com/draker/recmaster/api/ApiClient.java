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
 * Клиент для доступа к API TMDB
 */
public class ApiClient {

    private static final String TAG = "ApiClient";
    private static final String BASE_URL = BuildConfig.TMDB_BASE_URL;
    private static final String ACCESS_TOKEN = BuildConfig.TMDB_ACCESS_TOKEN;
    private static Retrofit retrofit = null;
    private static TmdbApi tmdbApi = null;

    /**
     * Получение экземпляра Retrofit
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
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
                    
                    // Добавляем Bearer токен в заголовок
                    Request newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + ACCESS_TOKEN)
                            .header("accept", "application/json")
                            .build();
                    
                    Log.d(TAG, "Sending request with Authorization header: Bearer " + ACCESS_TOKEN);
                    return chain.proceed(newRequest);
                }
            };

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(loggingInterceptor);
            httpClient.addInterceptor(authInterceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            
            Log.d(TAG, "Retrofit client initialized with base URL: " + BASE_URL);
        }
        return retrofit;
    }

    /**
     * Получение API интерфейса для TMDB
     */
    public static TmdbApi getTmdbApi() {
        if (tmdbApi == null) {
            tmdbApi = getClient().create(TmdbApi.class);
            Log.d(TAG, "TMDB API interface created");
        }
        return tmdbApi;
    }
}