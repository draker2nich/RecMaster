package com.draker.recmaster.ui.collection;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.draker.recmaster.R;
import com.draker.recmaster.adapter.ContentCollectionAdapter;
import com.draker.recmaster.databinding.FragmentCollectionBinding;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * Фрагмент для отображения коллекции пользователя
 * с вкладками для разных типов контента
 */
public class CollectionFragment extends Fragment {

    private static final String TAG = "CollectionFragment";
    private FragmentCollectionBinding binding;
    private TabLayoutMediator tabLayoutMediator;
    private ContentCollectionAdapter pagerAdapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

        setupViewPager();
        setupTabLayout();
    }
    
    /**
     * Настройка ViewPager2 для навигации между типами контента
     */
    private void setupViewPager() {
        // Создаем адаптер для ViewPager2
        pagerAdapter = new ContentCollectionAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);
        // Отключаем свайп между страницами (опционально)
        // binding.viewPager.setUserInputEnabled(false);
    }
    
    /**
     * Настройка TabLayout и связывание его с ViewPager2
     */
    private void setupTabLayout() {
        // Создаем медиатор для связи TabLayout и ViewPager2
        tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.tab_movies);
                    break;
                case 1:
                    tab.setText(R.string.tab_tv_shows);
                    break;
                case 2:
                    tab.setText(R.string.tab_books);
                    break;
                case 3:
                    tab.setText(R.string.tab_games);
                    break;
            }
        });
        // Применяем конфигурацию медиатора
        tabLayoutMediator.attach();
    }
    
    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        if (tabLayoutMediator != null) {
            tabLayoutMediator.detach();
        }
        binding.viewPager.setAdapter(null);
        super.onDestroyView();
        binding = null;
    }
    
    /**
     * Вложенный фрагмент для отображения пустых вкладок,
     * которые будут реализованы позже
     */
    public static class EmptyCollectionFragment extends Fragment {
        
        private final String title;
        
        public EmptyCollectionFragment(String title) {
            this.title = title;
        }
        
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Log.d("EmptyFragment", "onCreateView: " + title);
            return inflater.inflate(R.layout.fragment_collection_simple, container, false);
        }
        
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Log.d("EmptyFragment", "onViewCreated: " + title);
            TextView textView = view.findViewById(R.id.text_collection_simple);
            textView.setText(title);
        }
        
        @Override
        public void onDestroyView() {
            Log.d("EmptyFragment", "onDestroyView: " + title);
            super.onDestroyView();
        }
    }
}