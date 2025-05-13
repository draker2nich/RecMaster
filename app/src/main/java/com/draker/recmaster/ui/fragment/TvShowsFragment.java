package com.draker.recmaster.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.draker.recmaster.R;
import com.draker.recmaster.adapter.TvShowAdapter;
import com.draker.recmaster.model.TvShow;
import com.draker.recmaster.viewmodel.TvShowViewModel;

/**
 * Фрагмент для отображения сериалов
 */
public class TvShowsFragment extends Fragment implements TvShowAdapter.OnTvShowClickListener {

    private TvShowViewModel viewModel;
    private TvShowAdapter adapter;
    private RecyclerView recyclerView;
    private View loadingView;
    private View errorView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_shows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_tv_shows);
        loadingView = view.findViewById(R.id.loading_view);
        errorView = view.findViewById(R.id.error_view);

        // Настройка RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TvShowAdapter(requireContext(), this);
        recyclerView.setAdapter(adapter);

        // Настройка ViewModel
        viewModel = new ViewModelProvider(this).get(TvShowViewModel.class);
        
        // Наблюдение за данными
        viewModel.getTvShowsLiveData().observe(getViewLifecycleOwner(), tvShows -> {
            adapter.setTvShows(tvShows);
            recyclerView.setVisibility(View.VISIBLE);
        });

        viewModel.getGenresLiveData().observe(getViewLifecycleOwner(), genreMap -> {
            adapter.setGenreMap(genreMap);
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                showError(errorMessage);
            }
        });

        viewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading) {
                recyclerView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
            }
        });

        // Загрузка данных
        loadData();
    }

    /**
     * Загрузка данных о сериалах
     */
    private void loadData() {
        viewModel.getGenres();
        viewModel.getPopularTvShows(1);
    }

    /**
     * Отображение ошибки
     */
    private void showError(String errorMessage) {
        errorView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTvShowClick(TvShow tvShow) {
        // Открытие детальной информации о сериале
        // TODO: Реализовать переход на экран деталей сериала
        Toast.makeText(requireContext(), "Выбран сериал: " + tvShow.getName(), Toast.LENGTH_SHORT).show();
    }
}