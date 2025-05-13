package com.draker.recmaster.database.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.draker.recmaster.database.AppDatabase;
import com.draker.recmaster.database.dao.BookDao;
import com.draker.recmaster.database.entity.BookEntity;
import com.draker.recmaster.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с локальной базой данных книг
 */
public class LocalBookRepository {

    private static final String TAG = "LocalBookRepo";
    private static LocalBookRepository instance;
    private final BookDao bookDao;

    private LocalBookRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        bookDao = db.bookDao();
        Log.d(TAG, "LocalBookRepository initialized");
    }

    public static synchronized LocalBookRepository getInstance(Application application) {
        if (instance == null) {
            instance = new LocalBookRepository(application);
        }
        return instance;
    }

    /**
     * Вставка книги в базу данных
     */
    public void insertBook(BookEntity bookEntity) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            bookDao.insert(bookEntity);
            Log.d(TAG, "Book inserted: " + bookEntity.getTitle());
        });
    }

    /**
     * Вставка списка книг в базу данных
     */
    public void insertBooks(List<BookEntity> bookEntities) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            bookDao.insertAll(bookEntities);
            Log.d(TAG, "Inserted " + bookEntities.size() + " books");
        });
    }

    /**
     * Получение всех книг из базы данных
     */
    public LiveData<List<BookEntity>> getAllBooks() {
        return bookDao.getAllBooks();
    }

    /**
     * Получение книги по ID
     */
    public LiveData<BookEntity> getBookById(String bookId) {
        return bookDao.getBookById(bookId);
    }

    /**
     * Поиск книг по названию
     */
    public LiveData<List<BookEntity>> searchBooks(String query) {
        return bookDao.searchBooks(query);
    }

    /**
     * Поиск книг по автору
     */
    public LiveData<List<BookEntity>> getBooksByAuthor(String author) {
        return bookDao.getBooksByAuthor(author);
    }

    /**
     * Удаление книги из базы данных
     */
    public void deleteBook(String bookId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            bookDao.deleteBook(bookId);
            Log.d(TAG, "Book deleted: " + bookId);
        });
    }

    /**
     * Удаление всех книг из базы данных
     */
    public void deleteAllBooks() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            bookDao.deleteAllBooks();
            Log.d(TAG, "All books deleted");
        });
    }

    /**
     * Получение количества сохраненных книг
     */
    public int getBookCount() {
        return bookDao.getBookCount();
    }

    /**
     * Конвертирует объекты Book из API в сущности для базы данных
     */
    public List<BookEntity> booksToEntities(List<Book> books) {
        List<BookEntity> entities = new ArrayList<>();
        for (Book book : books) {
            entities.add(BookEntity.fromBook(book));
        }
        return entities;
    }

    /**
     * Конвертирует сущности из базы данных в объекты Book
     */
    public List<Book> entitiesToBooks(List<BookEntity> entities) {
        List<Book> books = new ArrayList<>();
        for (BookEntity entity : entities) {
            books.add(entity.toBook());
        }
        return books;
    }
}