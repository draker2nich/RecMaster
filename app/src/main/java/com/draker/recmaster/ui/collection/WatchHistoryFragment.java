package com.draker.recmaster.ui.collection;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.DividerItemDecoration;

import com.draker.recmaster.R;
import com.draker.recmaster.adapter.WatchHistoryAdapter;
import com.draker.recmaster.data.UserPreferences;
import com.draker.recmaster.database.entity.MovieEntity;
import com.draker.recmaster.databinding.FragmentWatchHistoryBinding;
import com.draker.recmaster.model.Movie;
import com.draker.recmaster.repository.MovieRepository;
import com.draker.recmaster.viewmodel.MovieViewModel;
import com.draker.recmaster.viewmodel.WatchHistoryViewModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Фрагмент для отображения истории просмотров фильмов пользователя
 */
public class WatchHistoryFragment extends Fragment implements WatchHistoryAdapter.OnItemClickListener {

    private static final String TAG = "WatchHistoryFragment";
    private FragmentWatchHistoryBinding binding;
    private WatchHistoryViewModel watchHistoryViewModel;
    private MovieViewModel movieViewModel;
    private WatchHistoryAdapter adapter;
    private UserPreferences userPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        userPreferences = new UserPreferences(requireContext());
        // Инициализируем ViewModel здесь, чтобы он был создан только один раз за время жизни фрагмента
        setupViewModels();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        binding = FragmentWatchHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

        setupRecyclerView();
        observeViewModel();
    }

    private void setupViewModels() {
        Log.d(TAG, "setupViewModels called");
        try {
            if (movieViewModel == null) {
                movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
                Log.d(TAG, "MovieViewModel initialized");
            }
            
            // Инициализация ViewModel с передачей Application context
            if (watchHistoryViewModel == null) {
                watchHistoryViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
                        .create(WatchHistoryViewModel.class);
                Log.d(TAG, "WatchHistoryViewModel initialized");
                
                // Устанавливаем текущего пользователя
                String currentUsername = userPreferences.getUsername();
                watchHistoryViewModel.setCurrentUser(currentUsername);
                Log.d(TAG, "Current username set: " + currentUsername);
            }
            
            // Загружаем жанры для отображения информации о фильмах
            movieViewModel.loadGenres();
            Log.d(TAG, "Genres loaded");
        } catch (Exception e) {
            Log.e(TAG, "Error in setupViewModels", e);
        }
    }

    private void setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView");
        // Создаем адаптер только один раз
        if (adapter == null) {
            adapter = new WatchHistoryAdapter(requireContext(), this);
            binding.rvWatchHistory.setAdapter(adapter);
            binding.rvWatchHistory.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        }
    }

    private void observeViewModel() {
        Log.d(TAG, "observeViewModel called");
        try {
            // Только устанавливаем обсерверы, если фрагмент присоединен
            if (isAdded() && getViewLifecycleOwner() != null) {
                // Наблюдаем за списком просмотренных фильмов
                watchHistoryViewModel.getWatchedMovies().observe(getViewLifecycleOwner(), movies -> {
                    Log.d(TAG, "Watched movies updated: " + (movies != null ? movies.size() : 0));
                    handleWatchedMovies(movies);
                });
                
                // Наблюдаем за картой жанров для отображения названий жанров
                movieViewModel.getGenreMap().observe(getViewLifecycleOwner(), genreMap -> {
                    Log.d(TAG, "Genre map updated: " + (genreMap != null ? genreMap.size() : 0));
                    handleGenreMap(genreMap);
                });
                
                // Наблюдаем за сообщениями об ошибках
                watchHistoryViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
                    Log.d(TAG, "Error message updated: " + error);
                    showError(error);
                });
            } else {
                Log.e(TAG, "Fragment not added or ViewLifecycleOwner is null");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in observeViewModel", e);
        }
    }

    private void handleWatchedMovies(List<MovieEntity> movies) {
        if (movies != null && !movies.isEmpty()) {
            adapter.setWatchedMovies(movies);
            showContent();
        } else {
            showEmptyState();
        }
    }

    private void handleGenreMap(Map<Integer, String> genreMap) {
        if (genreMap != null) {
            adapter.setGenreMap(genreMap);
        }
    }

    private void showContent() {
        binding.progressBar.setVisibility(View.GONE);
        binding.rvWatchHistory.setVisibility(View.VISIBLE);
        binding.tvNoHistory.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        binding.progressBar.setVisibility(View.GONE);
        binding.rvWatchHistory.setVisibility(View.GONE);
        binding.tvNoHistory.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvWatchHistory.setVisibility(View.GONE);
        binding.tvNoHistory.setVisibility(View.GONE);
    }

    private void showError(String error) {
        if (error != null && !error.isEmpty()) {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        // Очищаем ссылки для предотвращения утечек памяти
        if (binding != null && binding.rvWatchHistory != null) {
            binding.rvWatchHistory.setAdapter(null);
        }
        adapter = null;
        super.onDestroyView();
        binding = null;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        // Не очищаем ViewModels здесь, так как они привязаны к жизненному циклу активности/фрагмента
    }

    @Override
    public void onItemClick(MovieEntity movie) {
        // Здесь можно открыть детальную информацию о фильме
        // Например, создать и показать фрагмент с деталями
        Toast.makeText(requireContext(), "Открыть детали фильма: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
    }
}