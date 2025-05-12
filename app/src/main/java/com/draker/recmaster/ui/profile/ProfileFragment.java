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

import com.draker.recmaster.R;
import com.draker.recmaster.data.UserPreferences;
import com.draker.recmaster.model.User;
import com.draker.recmaster.ui.auth.LoginActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class ProfileFragment extends Fragment {

    private ImageView imageProfile;
    private TextView textUsername;
    private TextView textLevel;
    private LinearProgressIndicator progressLevel;
    private Button buttonEditProfile;
    private Button buttonLogout;
    private UserPreferences userPreferences;
    private User currentUser;

    private static final int REQUEST_EDIT_PROFILE = 100;
    private static final int REQUEST_SELECT_GENRES = 101;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_detail, container, false);

        userPreferences = new UserPreferences(requireContext());
        currentUser = userPreferences.loadUser();

        // Инициализация UI элементов
        imageProfile = view.findViewById(R.id.image_profile);
        textUsername = view.findViewById(R.id.text_username);
        textLevel = view.findViewById(R.id.text_level);
        progressLevel = view.findViewById(R.id.progress_level);
        buttonEditProfile = view.findViewById(R.id.button_edit_profile);
        buttonLogout = view.findViewById(R.id.button_logout);

        // Настраиваем слушатели
        buttonEditProfile.setOnClickListener(v -> openEditProfile());
        buttonLogout.setOnClickListener(v -> logout());

        // Обновляем UI с данными пользователя
        updateUI();

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

            // Если есть аватар, загружаем его
            if (currentUser.getAvatarUri() != null && !currentUser.getAvatarUri().isEmpty()) {
                // Используем Glide для загрузки изображения
                // Для простоты в этом примере не реализовано
            }
        }
    }

    private void openEditProfile() {
        Intent intent = new Intent(requireContext(), EditProfileActivity.class);
        startActivityForResult(intent, REQUEST_EDIT_PROFILE);
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