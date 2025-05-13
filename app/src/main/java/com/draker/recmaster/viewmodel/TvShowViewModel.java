package com.draker.recmaster.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.draker.recmaster.model.TvShow;
import com.draker.recmaster.repository.TvShowRepository;

import java.util.List;
import java.util.Map;

/**
 * ViewModel для работы с данными о сериалах
 */
public class TvShowViewModel extends AndroidViewModel {

    private static final String TAG = "TvShowViewModel";

    private final TvShowRepository tvShowRepository;
    private final MutableLiveData<List<TvShow>> tvShowsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Map<Integer, String>> genresLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>(false);

    public TvShowViewModel(@NonNull Application application) {
        super(application);
        tvShowRepository = TvShowRepository.getInstance();
    }

    /**
     * Получение списка популярных сериалов
     */
    public void getPopularTvShows(int page) {
        isLoadingLiveData.setValue(true);
        tvShowRepository.getPopularTvShows(tvShowsLiveData, errorLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получение списка сериалов с высоким рейтингом
     */
    public void getTopRatedTvShows(int page) {
        isLoadingLiveData.setValue(true);
        tvShowRepository.getTopRatedTvShows(tvShowsLiveData, errorLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получение списка сериалов, которые сейчас в эфире
     */
    public void getOnTheAirTvShows(int page) {
        isLoadingLiveData.setValue(true);
        tvShowRepository.getOnTheAirTvShows(tvShowsLiveData, errorLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получение списка сериалов, которые выходят сегодня
     */
    public void getAiringTodayTvShows(int page) {
        isLoadingLiveData.setValue(true);
        tvShowRepository.getAiringTodayTvShows(tvShowsLiveData, errorLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Поиск сериалов по запросу
     */
    public void searchTvShows(String query, int page) {
        isLoadingLiveData.setValue(true);
        tvShowRepository.searchTvShows(query, tvShowsLiveData, errorLiveData, page);
        isLoadingLiveData.setValue(false);
    }

    /**
     * Получение списка жанров сериалов
     */
    public void getGenres() {
        tvShowRepository.getTvShowGenres(genresLiveData, errorLiveData);
    }

    /**
     * Получить LiveData со списком сериалов
     */
    public MutableLiveData<List<TvShow>> getTvShowsLiveData() {
        return tvShowsLiveData;
    }

    /**
     * Получить LiveData с жанрами сериалов
     */
    public MutableLiveData<Map<Integer, String>> getGenresLiveData() {
        return genresLiveData;
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