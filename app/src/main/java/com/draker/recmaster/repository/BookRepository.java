package com.draker.recmaster.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.draker.recmaster.api.ApiClient;
import com.draker.recmaster.api.GoogleBooksApi;
import com.draker.recmaster.model.Book;
import com.draker.recmaster.model.BookResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Репозиторий для работы с данными о книгах
 */
public class BookRepository {

    private static final String TAG = "BookRepository";
    private static final String DEFAULT_LANGUAGE = "ru";
    private static final int DEFAULT_MAX_RESULTS = 20;

    private final GoogleBooksApi googleBooksApi;
    private static BookRepository instance;

    private BookRepository() {
        googleBooksApi = ApiClient.getGoogleBooksApi();
        Log.d(TAG, "BookRepository initialized");
    }

    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    /**
     * Поиск книг по запросу
     */
    public void searchBooks(String query, MutableLiveData<List<Book>> booksLiveData, MutableLiveData<String> errorLiveData, int startIndex) {
        Log.d(TAG, "Searching books with query: " + query + ", startIndex: " + startIndex);
        googleBooksApi.searchBooks(query, startIndex, DEFAULT_MAX_RESULTS, DEFAULT_LANGUAGE).enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                Log.d(TAG, "Search books response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = extractBooksFromResponse(response.body());
                    Log.d(TAG, "Found " + books.size() + " books for query: " + query);
                    booksLiveData.setValue(books);
                } else {
                    String errorMsg = "Ошибка поиска книг: " + response.code();
                    Log.e(TAG, errorMsg);
                    
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            errorMsg += " - " + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    
                    errorLiveData.setValue(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети при поиске книг: " + t.getMessage();
                Log.e(TAG, "Failed to search books", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение книг по категории
     */
    public void getBooksByCategory(String category, MutableLiveData<List<Book>> booksLiveData, MutableLiveData<String> errorLiveData, int startIndex) {
        Log.d(TAG, "Fetching books by category: " + category + ", startIndex: " + startIndex);
        String query = "subject:" + category;
        googleBooksApi.getBooksByCategory(query, startIndex, DEFAULT_MAX_RESULTS, DEFAULT_LANGUAGE).enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                Log.d(TAG, "Books by category response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = extractBooksFromResponse(response.body());
                    Log.d(TAG, "Found " + books.size() + " books for category: " + category);
                    booksLiveData.setValue(books);
                } else {
                    String errorMsg = "Ошибка загрузки книг по категории: " + response.code();
                    Log.e(TAG, errorMsg);
                    
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            errorMsg += " - " + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    
                    errorLiveData.setValue(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети при загрузке книг по категории: " + t.getMessage();
                Log.e(TAG, "Failed to get books by category", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение книг по автору
     */
    public void getBooksByAuthor(String author, MutableLiveData<List<Book>> booksLiveData, MutableLiveData<String> errorLiveData, int startIndex) {
        Log.d(TAG, "Fetching books by author: " + author + ", startIndex: " + startIndex);
        String query = "inauthor:" + author;
        googleBooksApi.getBooksByAuthor(query, startIndex, DEFAULT_MAX_RESULTS, DEFAULT_LANGUAGE).enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                Log.d(TAG, "Books by author response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = extractBooksFromResponse(response.body());
                    Log.d(TAG, "Found " + books.size() + " books for author: " + author);
                    booksLiveData.setValue(books);
                } else {
                    String errorMsg = "Ошибка загрузки книг по автору: " + response.code();
                    Log.e(TAG, errorMsg);
                    
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            errorMsg += " - " + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    
                    errorLiveData.setValue(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети при загрузке книг по автору: " + t.getMessage();
                Log.e(TAG, "Failed to get books by author", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Получение новинок книг
     */
    public void getNewReleases(MutableLiveData<List<Book>> booksLiveData, MutableLiveData<String> errorLiveData, int startIndex) {
        Log.d(TAG, "Fetching new releases, startIndex: " + startIndex);
        // Поиск по оригинальной дате публикации за последний год
        String query = "";
        String orderBy = "newest";
        googleBooksApi.getNewReleases(query, orderBy, startIndex, DEFAULT_MAX_RESULTS, DEFAULT_LANGUAGE).enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                Log.d(TAG, "New releases response received. Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = extractBooksFromResponse(response.body());
                    Log.d(TAG, "Found " + books.size() + " new release books");
                    booksLiveData.setValue(books);
                } else {
                    String errorMsg = "Ошибка загрузки новинок книг: " + response.code();
                    Log.e(TAG, errorMsg);
                    
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            errorMsg += " - " + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    
                    errorLiveData.setValue(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                String errorMsg = "Ошибка сети при загрузке новинок книг: " + t.getMessage();
                Log.e(TAG, "Failed to get new releases", t);
                errorLiveData.setValue(errorMsg);
            }
        });
    }

    /**
     * Извлекает список книг из ответа API
     */
    private List<Book> extractBooksFromResponse(BookResponse response) {
        List<Book> books = new ArrayList<>();
        if (response.getItems() != null) {
            for (BookResponse.BookItem item : response.getItems()) {
                if (item.getVolumeInfo() != null) {
                    Book book = item.getVolumeInfo();
                    book.setId(item.getId());
                    books.add(book);
                }
            }
        }
        return books;
    }
}