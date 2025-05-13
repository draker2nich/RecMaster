package com.draker.recmaster.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.draker.recmaster.database.entity.BookEntity;

import java.util.List;

/**
 * Data Access Object для работы с книгами в базе данных
 */
@Dao
public interface BookDao {

    /**
     * Вставка книги в базу данных
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BookEntity bookEntity);

    /**
     * Вставка списка книг в базу данных
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BookEntity> bookEntities);

    /**
     * Получение всех книг из базы данных
     */
    @Query("SELECT * FROM books ORDER BY savedTimestamp DESC")
    LiveData<List<BookEntity>> getAllBooks();

    /**
     * Получение книги по ID
     */
    @Query("SELECT * FROM books WHERE id = :bookId")
    LiveData<BookEntity> getBookById(String bookId);

    /**
     * Поиск книг по названию
     */
    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%'")
    LiveData<List<BookEntity>> searchBooks(String query);

    /**
     * Поиск книг по автору
     */
    @Query("SELECT * FROM books WHERE id IN (SELECT id FROM books WHERE authors LIKE '%' || :author || '%')")
    LiveData<List<BookEntity>> getBooksByAuthor(String author);

    /**
     * Удаление книги из базы данных
     */
    @Query("DELETE FROM books WHERE id = :bookId")
    void deleteBook(String bookId);

    /**
     * Удаление всех книг из базы данных
     */
    @Query("DELETE FROM books")
    void deleteAllBooks();

    /**
     * Получение количества сохраненных книг
     */
    @Query("SELECT COUNT(*) FROM books")
    int getBookCount();
}