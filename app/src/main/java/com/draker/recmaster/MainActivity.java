package com.draker.recmaster;

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
import com.draker.recmaster.service.GamificationService;
import com.draker.recmaster.ui.achievements.AchievementsFragment;
import com.draker.recmaster.ui.auth.LoginActivity;
import com.draker.recmaster.ui.collection.CollectionFragment;
import com.draker.recmaster.ui.home.HomeFragment;
import com.draker.recmaster.ui.profile.ProfileFragment;
import com.draker.recmaster.ui.recommendations.RecommendationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    private UserPreferences userPreferences;
    private GamificationService gamificationService;

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
        
        // Инициализируем сервис геймификации
        gamificationService = GamificationService.getInstance(this);
        
        // Инициализируем BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        
        // Настраиваем обработчик нажатий на элементы меню
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                
                try {
                    if (itemId == R.id.navigation_home) {
                        Log.d(TAG, "Выбрана вкладка Home");
                        loadFragment(new HomeFragment());
                        return true;
                    } else if (itemId == R.id.navigation_recommendations) {
                        Log.d(TAG, "Выбрана вкладка Recommendations");
                        loadFragment(new RecommendationsFragment());
                        return true;
                    } else if (itemId == R.id.navigation_collection) {
                        Log.d(TAG, "Выбрана вкладка Collection");
                        loadFragment(new CollectionFragment());
                        return true;
                    } else if (itemId == R.id.navigation_achievements) {
                        Log.d(TAG, "Выбрана вкладка Achievements");
                        loadFragment(new AchievementsFragment());
                        return true;
                    } else if (itemId == R.id.navigation_profile) {
                        Log.d(TAG, "Выбрана вкладка Profile");
                        loadFragment(new ProfileFragment());
                        return true;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Ошибка при переключении вкладки", e);
                }
                
                return false;
            }
        });
        
        // Проверяем, был ли запущен через уведомление о достижении
        if (getIntent() != null && getIntent().getBooleanExtra("OPEN_ACHIEVEMENTS", false)) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_achievements);
        } else {
            // По умолчанию показываем домашний фрагмент
            loadFragment(new HomeFragment());
        }
    }
    
    /**
     * Загружает фрагмент в контейнер, заменяя текущий
     */
    private void loadFragment(Fragment fragment) {
        String fragmentTag = fragment.getClass().getSimpleName();
        Log.d(TAG, "Loading fragment: " + fragmentTag);
        
        // Проверяем, не является ли уже отображаемый фрагмент тем же самым типом
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null && currentFragment.getClass().getName().equals(fragment.getClass().getName())) {
            Log.d(TAG, "This fragment is already shown: " + fragmentTag);
            return;
        }
        
        // Создаем транзакцию и заменяем текущий фрагмент
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, fragmentTag);
        transaction.commitNow(); // Используем commitNow для немедленного применения
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