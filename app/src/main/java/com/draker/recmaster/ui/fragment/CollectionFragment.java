package com.draker.recmaster.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.draker.recmaster.R;
import com.draker.recmaster.adapter.ContentCollectionAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * Фрагмент для отображения коллекции контента с табами для разных типов (фильмы, сериалы, книги, игры)
 */
public class CollectionFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ContentCollectionAdapter contentCollectionAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        // Инициализация адаптера для ViewPager
        contentCollectionAdapter = new ContentCollectionAdapter(this);
        viewPager.setAdapter(contentCollectionAdapter);

        // Настройка TabLayout
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
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
        }).attach();
    }
}