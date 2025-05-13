package com.draker.recmaster.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.draker.recmaster.model.Book;
import com.draker.recmaster.repository.BookRepository;

import java.util.List;

/**
 * ViewModel для работы с данными о книгах
 */
public class BookViewModel extends AndroidViewModel {

    private static final String TAG = "BookViewModel";

    private final BookRepository bookRepository;
    private final MutableLiveData<List<Book>> booksLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>(false);

    public BookViewModel(@NonNull Application application) {
        super(application);
        bookRepository = BookRepository.getInstance();
    }

    /**
     * Поиск книг по запросу
     */
    public void searchBooks(String query, int startIndex) {
        isLoadingLiveData.setValue(true);
        bookRepository.searchBooks(query, booksLiveData, errorLiveData, startIndex);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получение книг по категории
     */
    public void getBooksByCategory(String category, int startIndex) {
        isLoadingLiveData.setValue(true);
        bookRepository.getBooksByCategory(category, booksLiveData, errorLiveData, startIndex);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получение книг по автору
     */
    public void getBooksByAuthor(String author, int startIndex) {
        isLoadingLiveData.setValue(true);
        bookRepository.getBooksByAuthor(author, booksLiveData, errorLiveData, startIndex);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получение новинок книг
     */
    public void getNewReleases(int startIndex) {
        isLoadingLiveData.setValue(true);
        bookRepository.getNewReleases(booksLiveData, errorLiveData, startIndex);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получить LiveData со списком книг
     */
    public MutableLiveData<List<Book>> getBooksLiveData() {
        return booksLiveData;
    }

    /**
     * Получить LiveData с ошибками
     */
    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    /**
     * Получить LiveData с состоянием загрузки
     */
    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }
}