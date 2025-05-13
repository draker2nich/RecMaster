package com.draker.recmaster.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.draker.recmaster.ui.collection.WatchHistoryFragment;
import com.draker.recmaster.ui.fragment.BooksFragment;
import com.draker.recmaster.ui.fragment.GamesFragment;
import com.draker.recmaster.ui.fragment.TvShowsFragment;

/**
 * Адаптер для отображения фрагментов с различными типами контента в ViewPager
 */
public class ContentCollectionAdapter extends FragmentStateAdapter {

    public ContentCollectionAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new WatchHistoryFragment(); // Используем WatchHistoryFragment для фильмов
            case 1:
                return new TvShowsFragment();
            case 2:
                return new BooksFragment();
            case 3:
                return new GamesFragment();
            default:
                return new WatchHistoryFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Фильмы, Сериалы, Книги, Игры
    }
}