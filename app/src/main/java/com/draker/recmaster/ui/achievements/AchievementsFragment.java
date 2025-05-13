package com.draker.recmaster.ui.achievements;

import android.app.Dialog;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.draker.recmaster.R;
import com.draker.recmaster.adapter.AchievementAdapter;
import com.draker.recmaster.database.entity.UserEntity;
import com.draker.recmaster.model.Achievement;
import com.draker.recmaster.util.PreferenceManager;

import java.util.List;

/**
 * Фрагмент для отображения достижений пользователя
 */
public class AchievementsFragment extends Fragment {
    
    private AchievementsViewModel viewModel;
    private AchievementAdapter adapter;
    
    // UI компоненты
    private TextView levelTextView;
    private TextView experienceTextView;
    private ProgressBar levelProgressBar;
    private TextView nextLevelTextView;
    private TextView achievementsCountTextView;
    private RecyclerView achievementsRecyclerView;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AchievementsViewModel.class);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);
        
        // Инициализация UI компонентов
        levelTextView = view.findViewById(R.id.text_level);
        experienceTextView = view.findViewById(R.id.text_experience);
        levelProgressBar = view.findViewById(R.id.progress_level);
        nextLevelTextView = view.findViewById(R.id.text_next_level);
        achievementsCountTextView = view.findViewById(R.id.text_achievements_count);
        achievementsRecyclerView = view.findViewById(R.id.recycler_achievements);
        
        // Настройка адаптера
        adapter = new AchievementAdapter(requireContext());
        achievementsRecyclerView.setAdapter(adapter);
        
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Получение текущего пользователя
        String username = new PreferenceManager(requireContext()).getCurrentUsername();
        
        // Наблюдение за данными пользователя (уровень, опыт)
        viewModel.getUserStats(username).observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity user) {
                if (user != null) {
                    updateUserStatsUI(user);
                }
            }
        });
        
        // Наблюдение за списком достижений
        viewModel.getUserAchievements(username).observe(getViewLifecycleOwner(), new Observer<List<Achievement>>() {
            @Override
            public void onChanged(List<Achievement> achievements) {
                if (achievements != null) {
                    adapter.submitList(achievements);
                    updateAchievementsCountUI(achievements);
                }
            }
        });
        
        // Наблюдение за новыми разблокированными достижениями
        viewModel.getNewlyUnlockedAchievements().observe(getViewLifecycleOwner(), new Observer<List<Achievement>>() {
            @Override
            public void onChanged(List<Achievement> achievements) {
                if (achievements != null && !achievements.isEmpty()) {
                    for (Achievement achievement : achievements) {
                        showAchievementUnlockedDialog(achievement);
                    }
                }
            }
        });
    }
    
    /**
     * Обновляет UI со статистикой пользователя (уровень, опыт)
     */
    private void updateUserStatsUI(UserEntity user) {
        int level = user.getLevel();
        int experience = user.getExperience();
        int expForCurrentLevel = (level - 1) * 100;
        int expForNextLevel = level * 100;
        
        // Обновляем текст уровня
        levelTextView.setText("Уровень " + level);
        
        // Обновляем текст опыта
        experienceTextView.setText(experience + " XP / " + expForNextLevel + " XP");
        
        // Обновляем прогресс-бар
        int progressPercentage = (int)(viewModel.getLevelProgressPercentage(user) * 100);
        levelProgressBar.setProgress(progressPercentage);
        
        // Обновляем текст до следующего уровня
        int expToNextLevel = expForNextLevel - experience;
        nextLevelTextView.setText("Еще " + expToNextLevel + " XP до уровня " + (level + 1));
    }
    
    /**
     * Обновляет счетчик разблокированных достижений
     */
    private void updateAchievementsCountUI(List<Achievement> achievements) {
        int totalAchievements = achievements.size();
        int unlockedAchievements = 0;
        
        for (Achievement achievement : achievements) {
            if (achievement.isUnlocked()) {
                unlockedAchievements++;
            }
        }
        
        achievementsCountTextView.setText(unlockedAchievements + " / " + totalAchievements);
    }
    
    /**
     * Отображает диалог с информацией о разблокированном достижении
     */
    private void showAchievementUnlockedDialog(Achievement achievement) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_achievement_unlocked);
        
        // Настройка UI элементов диалога
        ImageView iconImageView = dialog.findViewById(R.id.image_achievement);
        TextView titleTextView = dialog.findViewById(R.id.text_achievement_title);
        TextView descriptionTextView = dialog.findViewById(R.id.text_achievement_description);
        TextView rewardTextView = dialog.findViewById(R.id.text_achievement_reward);
        Button okButton = dialog.findViewById(R.id.button_ok);
        
        // Установка данных достижения
        titleTextView.setText(achievement.getTitle());
        descriptionTextView.setText(achievement.getDescription());
        rewardTextView.setText("+" + achievement.getPointsReward() + " XP");
        
        // Установка иконки
        int resourceId = getResources().getIdentifier(
                achievement.getIconResourceName(), "drawable", requireContext().getPackageName());
        
        if (resourceId != 0) {
            iconImageView.setImageResource(resourceId);
        } else {
            iconImageView.setImageResource(R.drawable.ic_achievement_notification);
        }
        
        // Настройка кнопки
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        
        // Показываем диалог
        dialog.show();
    }
}