package com.draker.recmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.draker.recmaster.data.UserPreferences;
import com.draker.recmaster.ui.auth.LoginActivity;
import com.draker.recmaster.ui.collection.CollectionFragment;
import com.draker.recmaster.ui.home.HomeFragment;
import com.draker.recmaster.ui.profile.ProfileFragment;
import com.draker.recmaster.ui.recommendations.RecommendationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private RecommendationsFragment recommendationsFragment;
    private CollectionFragment collectionFragment;
    private ProfileFragment profileFragment;
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
        
        // Инициализируем фрагменты
        homeFragment = new HomeFragment();
        recommendationsFragment = new RecommendationsFragment();
        collectionFragment = new CollectionFragment();
        profileFragment = new ProfileFragment();
        
        // По умолчанию показываем домашний фрагмент
        setFragment(homeFragment);
        
        // Инициализируем BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        
        // Настраиваем обработчик нажатий на элементы меню
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                
                if (itemId == R.id.navigation_home) {
                    setFragment(homeFragment);
                    return true;
                } else if (itemId == R.id.navigation_recommendations) {
                    setFragment(recommendationsFragment);
                    return true;
                } else if (itemId == R.id.navigation_collection) {
                    setFragment(collectionFragment);
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    setFragment(profileFragment);
                    return true;
                }
                
                return false;
            }
        });
    }
    
    // Метод для установки текущего фрагмента
    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}