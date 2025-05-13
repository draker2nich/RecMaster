package com.draker.recmaster.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер для ViewPager2 в коллекции контента
 */
public class CollectionViewPagerAdapter extends FragmentStateAdapter {
    
    private static final String TAG = "CollectionVPAdapter";
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentTitles = new ArrayList<>();
    
    public CollectionViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        Log.d(TAG, "Создан с FragmentActivity");
    }
    
    public CollectionViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment.getChildFragmentManager(), fragment.getLifecycle());
        Log.d(TAG, "Создан с Fragment");
    }
    
    public CollectionViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        Log.d(TAG, "Создан с FragmentManager и Lifecycle");
    }
    
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d(TAG, "createFragment для позиции: " + position);
        return fragments.get(position);
    }
    
    @Override
    public int getItemCount() {
        return fragments.size();
    }
    
    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        fragmentTitles.add(title);
        Log.d(TAG, "Добавлен фрагмент: " + title + ", всего: " + fragments.size());
    }
    
    public String getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
}