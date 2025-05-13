package com.draker.recmaster.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.draker.recmaster.R;
import com.draker.recmaster.data.UserPreferences;
import com.draker.recmaster.database.entity.UserEntity;
import com.draker.recmaster.model.User;
import com.draker.recmaster.service.GamificationService;
import com.draker.recmaster.ui.auth.LoginActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class ProfileFragment extends Fragment {

    private ImageView imageProfile;
    private TextView textUsername;
    private TextView textLevel;
    private TextView textExperience;
    private LinearProgressIndicator progressLevel;
    private Button buttonEditProfile;
    private Button buttonLogout;
    private Button buttonAchievements;
    private UserPreferences userPreferences;
    private User currentUser;
    private GamificationService gamificationService;

    private static final int REQUEST_EDIT_PROFILE = 100;
    private static final int REQUEST_SELECT_GENRES = 101;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_detail, container, false);

        userPreferences = new UserPreferences(requireContext());
        currentUser = userPreferences.loadUser();
        gamificationService = GamificationService.getInstance(requireContext());

        // Инициализация UI элементов
        imageProfile = view.findViewById(R.id.image_profile);
        textUsername = view.findViewById(R.id.text_username);
        textLevel = view.findViewById(R.id.text_level);
        textExperience = view.findViewById(R.id.text_experience);
        progressLevel = view.findViewById(R.id.progress_level);
        buttonEditProfile = view.findViewById(R.id.button_edit_profile);
        buttonLogout = view.findViewById(R.id.button_logout);
        buttonAchievements = view.findViewById(R.id.button_achievements);

        // Настраиваем слушатели
        buttonEditProfile.setOnClickListener(v -> openEditProfile());
        buttonLogout.setOnClickListener(v -> logout());
        buttonAchievements.setOnClickListener(v -> openAchievements());

        // Обновляем UI с данными пользователя
        updateUI();

        // Наблюдаем за изменениями в статистике пользователя
        String username = userPreferences.getUsername();
        gamificationService.getUserStats(username).observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                if (userEntity != null) {
                    updateUIWithUserStats(userEntity);
                }
            }
        });

        return view;
    }

    private void updateUI() {
        if (currentUser != null) {
            textUsername.setText(currentUser.getUsername());
            textLevel.setText(getString(R.string.level_placeholder, currentUser.getLevel()));

            // Рассчитываем прогресс до следующего уровня
            int experience = currentUser.getExperience();
            int currentLevelExp = (currentUser.getLevel() - 1) * 100;
            int progress = experience - currentLevelExp;
            progressLevel.setProgress(progress);

            // Отображаем опыт
            int nextLevelExp = currentUser.getLevel() * 100;
            textExperience.setText(experience + " / " + nextLevelExp + " XP");

            // Если есть аватар, загружаем его
            if (currentUser.getAvatarUri() != null && !currentUser.getAvatarUri().isEmpty()) {
                // Используем Glide для загрузки изображения
                // Для простоты в этом примере не реализовано
            }
        }
    }

    private void updateUIWithUserStats(UserEntity userEntity) {
        textLevel.setText(getString(R.string.level_placeholder, userEntity.getLevel()));

        // Рассчитываем прогресс до следующего уровня
        int experience = userEntity.getExperience();
        int level = userEntity.getLevel();
        int currentLevelExp = (level - 1) * 100;
        int nextLevelExp = level * 100;
        int expToNextLevel = nextLevelExp - experience;
        
        // Рассчитываем процент завершения текущего уровня
        int expInCurrentLevel = experience - currentLevelExp;
        int expNeededForCurrentLevel = 100; // Каждый уровень требует 100 XP
        int progressPercentage = (int)(((float)expInCurrentLevel / expNeededForCurrentLevel) * 100);
        
        // Обновляем прогресс-бар и текст с опытом
        progressLevel.setProgress(progressPercentage);
        textExperience.setText(experience + " / " + nextLevelExp + " XP");
    }

    private void openEditProfile() {
        Intent intent = new Intent(requireContext(), EditProfileActivity.class);
        startActivityForResult(intent, REQUEST_EDIT_PROFILE);
    }
    
    private void openAchievements() {
        // Используем навигацию для перехода на экран достижений
        requireActivity().findViewById(R.id.navigation_achievements).performClick();
    }

    private void logout() {
        userPreferences.logoutUser();
        
        // Переходим на экран входа
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == requireActivity().RESULT_OK) {
            // Обновляем данные пользователя и UI
            currentUser = userPreferences.loadUser();
            updateUI();
        }
    }
}