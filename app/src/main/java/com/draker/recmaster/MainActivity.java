package com.draker.recmaster;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.draker.recmaster.data.UserPreferences;
import com.draker.recmaster.database.repository.LocalMovieRepository;
import com.draker.recmaster.repository.MovieRepository;
import com.draker.recmaster.ui.auth.LoginActivity;
import com.draker.recmaster.ui.collection.CollectionFragment;
import com.draker.recmaster.ui.home.HomeFragment;
import com.draker.recmaster.ui.profile.ProfileFragment;
import com.draker.recmaster.ui.recommendations.RecommendationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static HomeFragment homeFragment;
    private static RecommendationsFragment recommendationsFragment;
    private static CollectionFragment collectionFragment;
    private static ProfileFragment profileFragment;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Проверяем авторизацию
        userPreferences = new UserPreferences(this);
        if (!userPreferences.isUserLoggedIn()) {
            // Если пользователь не авторизован, перенаправляем на экран авторизации
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        setContentView(R.layout.activity_main);
        
        // Инициализируем репозитории и настраиваем доступ к базе данных
        initRepositories();
        
        // Инициализируем фрагменты
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        
        if (recommendationsFragment == null) {
            recommendationsFragment = new RecommendationsFragment();
        }
        
        if (collectionFragment == null) {
            collectionFragment = new CollectionFragment();
        }
        
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        
        // По умолчанию показываем домашний фрагмент
        setFragment(homeFragment);
        
        // Инициализируем BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        
        // Настраиваем обработчик нажатий на элементы меню
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                
                try {
                    if (itemId == R.id.navigation_home) {
                        Log.d(TAG, "Выбрана вкладка Home");
                        setFragment(homeFragment);
                        return true;
                    } else if (itemId == R.id.navigation_recommendations) {
                        Log.d(TAG, "Выбрана вкладка Recommendations");
                        setFragment(recommendationsFragment);
                        return true;
                    } else if (itemId == R.id.navigation_collection) {
                        Log.d(TAG, "Выбрана вкладка Collection");
                        setFragment(collectionFragment);
                        return true;
                    } else if (itemId == R.id.navigation_profile) {
                        Log.d(TAG, "Выбрана вкладка Profile");
                        setFragment(profileFragment);
                        return true;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Ошибка при переключении вкладки", e);
                }
                
                return false;
            }
        });
    }
    
    // Метод для установки текущего фрагмента с сохранением состояния
    private void setFragment(Fragment fragment) {
        String fragmentName = fragment.getClass().getSimpleName();
        Log.d("MainActivity", "Переключаемся на фрагмент: " + fragmentName);
        
        // Проверяем, нужно ли добавлять новый экземпляр фрагмента
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            Log.d("MainActivity", "Фрагмент того же типа уже активен");
            return;
        }
        
        // Используем фрагмент-менеджер для транзакции
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        
        // Сохраняем фрагменты в стеке, чтобы они не пересоздавались
        transaction.replace(R.id.fragment_container, fragment, fragmentName);
        
        // Применяем транзакцию без ожидания возобновления жизненного цикла
        transaction.commitNowAllowingStateLoss();
    }
    
    private void initRepositories() {
        // Устанавливаем связь между сетевым и локальным репозиториями
        MovieRepository movieRepository = MovieRepository.getInstance();
        movieRepository.setLocalRepository(getApplication());
        
        // Инициализируем локальный репозиторий
        LocalMovieRepository.getInstance(getApplication());
        
        Log.d("MainActivity", "Repositories initialized");
    }
}