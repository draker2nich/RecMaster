package com.draker.recmaster.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.draker.recmaster.R;
import com.draker.recmaster.adapter.MovieAdapter;
import com.draker.recmaster.model.Movie;
import com.draker.recmaster.util.NetworkUtil;
import com.draker.recmaster.viewmodel.MovieViewModel;

public class HomeFragment extends Fragment implements MovieAdapter.OnMovieClickListener {

    private static final String TAG = "HomeFragment";
    private MovieViewModel movieViewModel;
    private MovieAdapter popularAdapter;
    private MovieAdapter topRatedAdapter;
    private MovieAdapter upcomingAdapter;
    private RecyclerView recyclerPopularMovies;
    private RecyclerView recyclerTopRatedMovies;
    private RecyclerView recyclerUpcomingMovies;
    private ProgressBar progressPopular;
    private ProgressBar progressTopRated;
    private ProgressBar progressUpcoming;
    private TextView textError;
    private Button buttonRetry;
    private Group groupContent;
    private Group groupError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация views
        initViews(view);

        // Настройка RecyclerViews
        setupRecyclerViews();

        // Инициализация ViewModel
        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);

        // Настройка observers
        observeViewModel();

        // Загрузка данных
        checkNetworkAndLoadData();
    }

    private void initViews(View view) {
        recyclerPopularMovies = view.findViewById(R.id.recycler_popular_movies);
        recyclerTopRatedMovies = view.findViewById(R.id.recycler_top_rated_movies);
        recyclerUpcomingMovies = view.findViewById(R.id.recycler_upcoming_movies);
        progressPopular = view.findViewById(R.id.progress_popular);
        progressTopRated = view.findViewById(R.id.progress_top_rated);
        progressUpcoming = view.findViewById(R.id.progress_upcoming);
        textError = view.findViewById(R.id.text_error);
        buttonRetry = view.findViewById(R.id.button_retry);
        groupContent = view.findViewById(R.id.group_content);
        groupError = view.findViewById(R.id.group_error);

        Log.d(TAG, "HomeFragment views initialized");

        // Изначально скрываем контент и показываем загрузку
        showLoadingState();

        // Установка обработчика для кнопки повторной попытки
        buttonRetry.setOnClickListener(v -> {
            Log.d(TAG, "Retry button clicked");
            showLoadingState();
            checkNetworkAndLoadData();
        });

        // Настройка кнопок "Показать все"
        view.findViewById(R.id.text_see_all_popular).setOnClickListener(v -> {
            // TODO: Переход к полному списку популярных фильмов
            Toast.makeText(requireContext(), "Показать все популярные фильмы", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.text_see_all_top_rated).setOnClickListener(v -> {
            // TODO: Переход к полному списку топ-рейтинговых фильмов
            Toast.makeText(requireContext(), "Показать все топ-рейтинговые фильмы", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.text_see_all_upcoming).setOnClickListener(v -> {
            // TODO: Переход к полному списку предстоящих фильмов
            Toast.makeText(requireContext(), "Показать все предстоящие фильмы", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupRecyclerViews() {
        // Настройка адаптеров
        popularAdapter = new MovieAdapter(requireContext(), this);
        topRatedAdapter = new MovieAdapter(requireContext(), this);
        upcomingAdapter = new MovieAdapter(requireContext(), this);

        // Настройка RecyclerViews
        recyclerPopularMovies.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerPopularMovies.setAdapter(popularAdapter);

        recyclerTopRatedMovies.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerTopRatedMovies.setAdapter(topRatedAdapter);

        recyclerUpcomingMovies.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerUpcomingMovies.setAdapter(upcomingAdapter);
        
        Log.d(TAG, "RecyclerViews and adapters set up");
    }

    private void observeViewModel() {
        // Наблюдение за данными о фильмах
        movieViewModel.getPopularMovies().observe(getViewLifecycleOwner(), movies -> {
            Log.d(TAG, "Получено популярных фильмов: " + (movies != null ? movies.size() : 0));
            if (movies != null && !movies.isEmpty()) {
                popularAdapter.setMovies(movies);
                showContentView(); // Показываем контент, когда получены данные
            }
            progressPopular.setVisibility(View.GONE);
        });

        movieViewModel.getTopRatedMovies().observe(getViewLifecycleOwner(), movies -> {
            Log.d(TAG, "Получено фильмов с высоким рейтингом: " + (movies != null ? movies.size() : 0));
            if (movies != null && !movies.isEmpty()) {
                topRatedAdapter.setMovies(movies);
            }
            progressTopRated.setVisibility(View.GONE);
        });

        movieViewModel.getUpcomingMovies().observe(getViewLifecycleOwner(), movies -> {
            Log.d(TAG, "Получено предстоящих фильмов: " + (movies != null ? movies.size() : 0));
            if (movies != null && !movies.isEmpty()) {
                upcomingAdapter.setMovies(movies);
            }
            progressUpcoming.setVisibility(View.GONE);
        });

        // Наблюдение за жанрами
        movieViewModel.getGenreMap().observe(getViewLifecycleOwner(), genreMap -> {
            Log.d(TAG, "Получено жанров: " + (genreMap != null ? genreMap.size() : 0));
            if (genreMap != null && !genreMap.isEmpty()) {
                popularAdapter.setGenreMap(genreMap);
                topRatedAdapter.setGenreMap(genreMap);
                upcomingAdapter.setGenreMap(genreMap);
            }
        });

        // Наблюдение за ошибками
        movieViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                textError.setText(errorMessage);
                showErrorView();
                Log.e(TAG, "Error received: " + errorMessage);
            }
        });

        // Наблюдение за состоянием загрузки
        movieViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoadingState();
            }
        });
        
        Log.d(TAG, "ViewModel observers set up");
    }

    // Проверяем подключение к интернету перед загрузкой данных
    private void checkNetworkAndLoadData() {
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            loadData();
        } else {
            // Если нет подключения, показываем ошибку
            textError.setText(R.string.no_internet_connection);
            showErrorView();
            Log.e(TAG, "No internet connection");
            Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }

    private void loadData() {
        Log.d(TAG, "Starting to load data");
        
        // Показываем загрузку
        showLoadingState();

        // Загрузка жанров
        movieViewModel.loadGenres();

        // Загрузка списков фильмов
        movieViewModel.loadPopularMovies(1);
        movieViewModel.loadTopRatedMovies(1);
        movieViewModel.loadUpcomingMovies(1);
    }

    // Показываем индикаторы загрузки
    private void showLoadingState() {
        progressPopular.setVisibility(View.VISIBLE);
        progressTopRated.setVisibility(View.VISIBLE);
        progressUpcoming.setVisibility(View.VISIBLE);
        groupContent.setVisibility(View.VISIBLE);
        groupError.setVisibility(View.GONE);
        Log.d(TAG, "Showing loading state");
    }

    // Показываем представление с контентом
    private void showContentView() {
        groupContent.setVisibility(View.VISIBLE);
        groupError.setVisibility(View.GONE);
        Log.d(TAG, "Showing content view");
    }

    // Показываем представление с ошибкой
    private void showErrorView() {
        groupContent.setVisibility(View.GONE);
        groupError.setVisibility(View.VISIBLE);
        Log.d(TAG, "Showing error view");
    }

    @Override
    public void onMovieClick(Movie movie) {
        // Обработка нажатия на фильм
        Toast.makeText(requireContext(), "Выбран фильм: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
        
        // TODO: Переход к деталям фильма
        // Intent intent = new Intent(requireContext(), MovieDetailsActivity.class);
        // intent.putExtra("movie_id", movie.getId());
        // startActivity(intent);
    }
}