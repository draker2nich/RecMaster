package com.draker.recmaster.ui.recommendations;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.draker.recmaster.R;
import com.draker.recmaster.adapter.GenreSelectionAdapter;
import com.draker.recmaster.adapter.MoodAdapter;
import com.draker.recmaster.adapter.MovieAdapter;
import com.draker.recmaster.model.Genre;
import com.draker.recmaster.model.Mood;
import com.draker.recmaster.model.Movie;
import com.draker.recmaster.model.RecommendationFilter;
import com.draker.recmaster.viewmodel.RecommendationsViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecommendationsFragment extends Fragment {
    private static final String TAG = "RecommendationsFragment";
    
    private RecommendationsViewModel viewModel;
    private MoodAdapter moodAdapter;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerMoods;
    private RecyclerView recyclerRecommendations;
    private ChipGroup chipGroupDuration;
    private ChipGroup chipGroupSelectedGenres;
    private View btnSelectGenres;
    private View textNoRecommendations;
    private FloatingActionButton fabResetFilters;
    
    // Карта жанров для поиска по ID
    private Map<Integer, String> genreMap;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommendations, container, false);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Инициализируем ViewModel
        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(RecommendationsViewModel.class);
        
        // Находим все необходимые View
        initViews(view);
        
        // Настраиваем RecyclerView для настроений
        setupMoodRecyclerView();
        
        // Настраиваем RecyclerView для рекомендаций
        setupRecommendationsRecyclerView();
        
        // Настраиваем обработчики для элементов фильтрации
        setupFilterListeners();
        
        // Подписываемся на изменения данных из ViewModel
        observeViewModelData();
    }
    
    private void initViews(View view) {
        recyclerMoods = view.findViewById(R.id.recycler_moods);
        recyclerRecommendations = view.findViewById(R.id.recycler_recommendations);
        chipGroupDuration = view.findViewById(R.id.chip_group_duration);
        chipGroupSelectedGenres = view.findViewById(R.id.chip_group_selected_genres);
        btnSelectGenres = view.findViewById(R.id.btn_select_genres);
        textNoRecommendations = view.findViewById(R.id.text_no_recommendations);
        fabResetFilters = view.findViewById(R.id.fab_reset_filters);
    }
    
    private void setupMoodRecyclerView() {
        Context context = requireContext();
        moodAdapter = new MoodAdapter(context);
        recyclerMoods.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerMoods.setAdapter(moodAdapter);
        
        // Обработчик выбора настроения
        moodAdapter.setOnMoodSelectedListener(mood -> {
            String moodId = mood.isSelected() ? mood.getId() : "";
            viewModel.setMood(moodId);
        });
    }
    
    private void setupRecommendationsRecyclerView() {
        Context context = requireContext();
        movieAdapter = new MovieAdapter(context, movie -> {
            // Действие при нажатии на фильм
            Toast.makeText(context, "Выбран фильм: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
            // Здесь можно добавить переход к деталям фильма
        });
        
        recyclerRecommendations.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerRecommendations.setAdapter(movieAdapter);
    }
    
    private void setupFilterListeners() {
        // Обработчик выбора продолжительности
        chipGroupDuration.setOnCheckedChangeListener((group, checkedId) -> {
            int maxDuration;
            
            if (checkedId == R.id.chip_duration_short) {
                maxDuration = RecommendationFilter.DURATION_SHORT;
            } else if (checkedId == R.id.chip_duration_medium) {
                maxDuration = RecommendationFilter.DURATION_MEDIUM; 
            } else if (checkedId == R.id.chip_duration_long) {
                maxDuration = RecommendationFilter.DURATION_LONG;
            } else {
                maxDuration = RecommendationFilter.DURATION_ANY;
            }
            
            viewModel.setMaxDuration(maxDuration);
        });
        
        // Обработчик нажатия на кнопку выбора жанров
        btnSelectGenres.setOnClickListener(v -> showGenreSelectionDialog());
        
        // Обработчик кнопки сброса фильтров
        fabResetFilters.setOnClickListener(v -> {
            viewModel.resetFilters();
            moodAdapter.clearSelection();
            chipGroupDuration.check(R.id.chip_duration_any);
            chipGroupSelectedGenres.removeAllViews();
            chipGroupSelectedGenres.setVisibility(View.GONE);
        });
    }
    
    private void observeViewModelData() {
        // Наблюдаем за списком настроений
        viewModel.getAvailableMoods().observe(getViewLifecycleOwner(), moods -> {
            if (moods != null) {
                moodAdapter.setMoods(moods);
            }
        });
        
        // Наблюдаем за списком жанров
        viewModel.getGenreMap().observe(getViewLifecycleOwner(), genreMap -> {
            if (genreMap != null) {
                this.genreMap = genreMap;
                movieAdapter.setGenreMap(genreMap);
            }
        });
        
        // Наблюдаем за списком рекомендуемых фильмов
        viewModel.getRecommendedMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                showMovies(movies);
            }
        });
        
        // Наблюдаем за текущим фильтром
        viewModel.getCurrentFilter().observe(getViewLifecycleOwner(), filter -> {
            if (filter != null) {
                updateFilterUI(filter);
            }
        });
        
        // Наблюдаем за сообщениями об ошибках
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showMovies(List<Movie> movies) {
        if (movies.isEmpty()) {
            textNoRecommendations.setVisibility(View.VISIBLE);
            recyclerRecommendations.setVisibility(View.GONE);
        } else {
            textNoRecommendations.setVisibility(View.GONE);
            recyclerRecommendations.setVisibility(View.VISIBLE);
            movieAdapter.setMovies(movies);
        }
    }
    
    private void updateFilterUI(RecommendationFilter filter) {
        // Обновляем UI выбранного настроения
        if (filter.getMood() != null && !filter.getMood().isEmpty()) {
            moodAdapter.selectMood(filter.getMood());
        }
        
        // Обновляем UI выбранной продолжительности
        int chipId;
        if (filter.getMaxDuration() == RecommendationFilter.DURATION_SHORT) {
            chipId = R.id.chip_duration_short;
        } else if (filter.getMaxDuration() == RecommendationFilter.DURATION_MEDIUM) {
            chipId = R.id.chip_duration_medium;
        } else if (filter.getMaxDuration() == RecommendationFilter.DURATION_LONG) {
            chipId = R.id.chip_duration_long;
        } else {
            chipId = R.id.chip_duration_any;
        }
        chipGroupDuration.check(chipId);
        
        // Обновляем UI выбранных жанров
        updateSelectedGenresChips(filter.getSelectedGenreIds());
    }
    
    private void updateSelectedGenresChips(List<Integer> selectedGenreIds) {
        chipGroupSelectedGenres.removeAllViews();
        
        if (selectedGenreIds == null || selectedGenreIds.isEmpty() || genreMap == null) {
            chipGroupSelectedGenres.setVisibility(View.GONE);
            return;
        }
        
        chipGroupSelectedGenres.setVisibility(View.VISIBLE);
        
        for (Integer genreId : selectedGenreIds) {
            String genreName = genreMap.get(genreId);
            if (genreName != null) {
                Chip chip = new Chip(requireContext());
                chip.setText(genreName);
                chip.setCloseIconVisible(true);
                
                chip.setOnCloseIconClickListener(v -> {
                    viewModel.removeGenreFromFilter(genreId);
                    chipGroupSelectedGenres.removeView(chip);
                    
                    if (chipGroupSelectedGenres.getChildCount() == 0) {
                        chipGroupSelectedGenres.setVisibility(View.GONE);
                    }
                });
                
                chipGroupSelectedGenres.addView(chip);
            }
        }
    }
    
    private void showGenreSelectionDialog() {
        if (genreMap == null || genreMap.isEmpty()) {
            Toast.makeText(requireContext(), R.string.error_loading_data, Toast.LENGTH_SHORT).show();
            return;
        }
        
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_genre_selection, null);
        RecyclerView recyclerGenres = dialogView.findViewById(R.id.recycler_genres);
        
        // Создаем список жанров из карты
        List<Genre> genres = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : genreMap.entrySet()) {
            genres.add(new Genre(entry.getKey(), entry.getValue()));
        }
        
        // Создаем и настраиваем адаптер
        GenreSelectionAdapter adapter = new GenreSelectionAdapter(requireContext());
        adapter.setGenres(genres);
        
        // Устанавливаем уже выбранные жанры
        if (viewModel.getCurrentFilter().getValue() != null) {
            adapter.setSelectedGenreIds(viewModel.getCurrentFilter().getValue().getSelectedGenreIds());
        }
        
        recyclerGenres.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerGenres.setAdapter(adapter);
        
        // Создаем диалог
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();
        
        // Настраиваем кнопки
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btn_apply).setOnClickListener(v -> {
            List<Integer> selectedIds = adapter.getSelectedGenreIds();
            viewModel.setSelectedGenres(selectedIds);
            dialog.dismiss();
        });
        
        dialog.show();
    }
}