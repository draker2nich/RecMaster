package com.draker.recmaster.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.draker.recmaster.MainActivity;
import com.draker.recmaster.R;
import com.draker.recmaster.data.UserPreferences;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private AuthPagerAdapter pagerAdapter;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Проверяем, авторизован ли пользователь
        userPreferences = new UserPreferences(this);
        if (userPreferences.isUserLoggedIn()) {
            // Если пользователь уже авторизован, сразу переходим на главный экран
            startMainActivity();
            return;
        }
        
        setContentView(R.layout.activity_login);
        
        tabLayout = findViewById(R.id.tabs_auth);
        viewPager = findViewById(R.id.view_pager_auth);
        
        // Настраиваем ViewPager и TabLayout
        pagerAdapter = new AuthPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        
        // Связываем TabLayout с ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.login);
                    break;
                case 1:
                    tab.setText(R.string.register);
                    break;
            }
        }).attach();
    }
    
    // Метод для перехода на главный экран
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Закрываем экран авторизации
    }
}