package com.draker.recmaster.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.draker.recmaster.R;
import com.draker.recmaster.model.Movie;
import com.draker.recmaster.viewmodel.MovieViewModel;

/**
 * Фрагмент для отображения фильмов
 */
public class MoviesFragment extends Fragment {

    private MovieViewModel viewModel;
    private RecyclerView recyclerView;
    private View loadingView;
    private View errorView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация компонентов фрагмента
        recyclerView = view.findViewById(R.id.recycler_view_movies);
        loadingView = view.findViewById(R.id.loading_view);
        errorView = view.findViewById(R.id.error_view);

        // Настройка RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        // Настройка ViewModel
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        
        // Загрузка данных о фильмах (здесь будет реализация в зависимости от вашего кода)
    }
}