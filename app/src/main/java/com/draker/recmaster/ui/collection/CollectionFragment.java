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
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.draker.recmaster.R;
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
    
    // Сохраняем ViewPager2 в качестве статической переменной, 
    // чтобы не создавать новый экземпляр при повторном открытии фрагмента
    private static CollectionPagerAdapter pagerAdapter;
    
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
        
        // Используем упрощенный подход, отказываемся от ViewPager для начала
        // и просто показываем фрагмент истории просмотров
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                .replace(R.id.viewPager, new WatchHistoryFragment())
                .commitNow();
        }
        
        // Настраиваем заголовок вкладки
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_movies));
    }
    
    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
        binding = null;
    }
    
    // Упрощенная версия для тестирования
    public static class CollectionPagerAdapter extends FragmentStateAdapter {
        private static final int NUM_PAGES = 1;
        
        public CollectionPagerAdapter(FragmentActivity fa) {
            super(fa);
            Log.d(TAG, "CollectionPagerAdapter created");
        }
        
        public CollectionPagerAdapter(Fragment fragment) {
            super(fragment);
            Log.d(TAG, "CollectionPagerAdapter created from fragment");
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Log.d(TAG, "createFragment at position: " + position);
            return new WatchHistoryFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
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