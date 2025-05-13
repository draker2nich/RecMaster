package com.draker.recmaster.database.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.draker.recmaster.database.AppDatabase;
import com.draker.recmaster.database.dao.TvShowDao;
import com.draker.recmaster.database.entity.TvShowEntity;
import com.draker.recmaster.model.TvShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с локальной базой данных сериалов
 */
public class LocalTvShowRepository {

    private static final String TAG = "LocalTvShowRepo";
    private static LocalTvShowRepository instance;
    private final TvShowDao tvShowDao;

    private LocalTvShowRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        tvShowDao = db.tvShowDao();
        Log.d(TAG, "LocalTvShowRepository initialized");
    }

    public static synchronized LocalTvShowRepository getInstance(Application application) {
        if (instance == null) {
            instance = new LocalTvShowRepository(application);
        }
        return instance;
    }

    /**
     * Вставка сериала в базу данных
     */
    public void insertTvShow(TvShowEntity tvShowEntity) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            tvShowDao.insert(tvShowEntity);
            Log.d(TAG, "TvShow inserted: " + tvShowEntity.getName());
        });
    }

    /**
     * Вставка списка сериалов в базу данных
     */
    public void insertTvShows(List<TvShowEntity> tvShowEntities) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            tvShowDao.insertAll(tvShowEntities);
            Log.d(TAG, "Inserted " + tvShowEntities.size() + " tv shows");
        });
    }

    /**
     * Получение всех сериалов из базы данных
     */
    public LiveData<List<TvShowEntity>> getAllTvShows() {
        return tvShowDao.getAllTvShows();
    }

    /**
     * Получение сериала по ID
     */
    public LiveData<TvShowEntity> getTvShowById(int tvShowId) {
        return tvShowDao.getTvShowById(tvShowId);
    }

    /**
     * Поиск сериалов по названию
     */
    public LiveData<List<TvShowEntity>> searchTvShows(String query) {
        return tvShowDao.searchTvShows(query);
    }

    /**
     * Удаление сериала из базы данных
     */
    public void deleteTvShow(int tvShowId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            tvShowDao.deleteTvShow(tvShowId);
            Log.d(TAG, "TvShow deleted: " + tvShowId);
        });
    }

    /**
     * Удаление всех сериалов из базы данных
     */
    public void deleteAllTvShows() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            tvShowDao.deleteAllTvShows();
            Log.d(TAG, "All tv shows deleted");
        });
    }

    /**
     * Получение количества сохраненных сериалов
     */
    public int getTvShowCount() {
        return tvShowDao.getTvShowCount();
    }

    /**
     * Конвертирует объекты TvShow из API в сущности для базы данных
     */
    public List<TvShowEntity> tvShowsToEntities(List<TvShow> tvShows) {
        List<TvShowEntity> entities = new ArrayList<>();
        for (TvShow tvShow : tvShows) {
            entities.add(TvShowEntity.fromTvShow(tvShow));
        }
        return entities;
    }

    /**
     * Конвертирует сущности из базы данных в объекты TvShow
     */
    public List<TvShow> entitiesToTvShows(List<TvShowEntity> entities) {
        List<TvShow> tvShows = new ArrayList<>();
        for (TvShowEntity entity : entities) {
            tvShows.add(entity.toTvShow());
        }
        return tvShows;
    }
}