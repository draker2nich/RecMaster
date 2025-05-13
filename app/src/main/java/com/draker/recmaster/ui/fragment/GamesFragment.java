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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.draker.recmaster.R;
import com.draker.recmaster.adapter.GameAdapter;
import com.draker.recmaster.model.Game;
import com.draker.recmaster.viewmodel.GameViewModel;

/**
 * Фрагмент для отображения игр
 */
public class GamesFragment extends Fragment implements GameAdapter.OnGameClickListener {

    private GameViewModel viewModel;
    private GameAdapter adapter;
    private RecyclerView recyclerView;
    private View loadingView;
    private View errorView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_games, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_games);
        loadingView = view.findViewById(R.id.loading_view);
        errorView = view.findViewById(R.id.error_view);

        // Настройка RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        adapter = new GameAdapter(requireContext(), this);
        recyclerView.setAdapter(adapter);

        // Настройка ViewModel
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        
        // Наблюдение за данными
        viewModel.getGamesLiveData().observe(getViewLifecycleOwner(), games -> {
            adapter.setGames(games);
            recyclerView.setVisibility(View.VISIBLE);
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
     * Загрузка данных об играх
     */
    private void loadData() {
        viewModel.getPopularGames(1);
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
    public void onGameClick(Game game) {
        // Открытие детальной информации об игре
        // TODO: Реализовать переход на экран деталей игры
        Toast.makeText(requireContext(), "Выбрана игра: " + game.getName(), Toast.LENGTH_SHORT).show();
    }
}