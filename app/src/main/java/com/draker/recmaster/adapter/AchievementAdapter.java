package com.draker.recmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.draker.recmaster.R;
import com.draker.recmaster.model.Achievement;

/**
 * Адаптер для отображения списка достижений
 */
public class AchievementAdapter extends ListAdapter<Achievement, AchievementAdapter.AchievementViewHolder> {

    private final Context context;
    
    public AchievementAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }
    
    private static final DiffUtil.ItemCallback<Achievement> DIFF_CALLBACK = new DiffUtil.ItemCallback<Achievement>() {
        @Override
        public boolean areItemsTheSame(@NonNull Achievement oldItem, @NonNull Achievement newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Achievement oldItem, @NonNull Achievement newItem) {
            return oldItem.isUnlocked() == newItem.isUnlocked() && 
                   oldItem.getCurrentProgress() == newItem.getCurrentProgress();
        }
    };
    
    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = getItem(position);
        
        // Установка заголовка и описания
        holder.titleTextView.setText(achievement.getTitle());
        holder.descriptionTextView.setText(achievement.getDescription());
        
        // Установка награды
        holder.rewardTextView.setText("+" + achievement.getPointsReward());
        
        // Установка иконки
        int resourceId = context.getResources().getIdentifier(
                achievement.getIconResourceName(), "drawable", context.getPackageName());
        
        if (resourceId != 0) {
            holder.iconImageView.setImageResource(resourceId);
        } else {
            holder.iconImageView.setImageResource(R.drawable.ic_achievement_notification);
        }
        
        // Установка прогресса
        int progress = achievement.getCurrentProgress();
        int threshold = achievement.getThreshold();
        
        if (achievement.isUnlocked()) {
            // Если достижение разблокировано, показываем полный прогресс
            holder.progressBar.setProgress(100);
            holder.progressTextView.setText(threshold + "/" + threshold);
            
            // Изменяем внешний вид для разблокированных достижений
            holder.itemView.setAlpha(1.0f);
        } else {
            // Если достижение не разблокировано, показываем текущий прогресс
            int progressPercentage = (int)(achievement.getProgressPercentage() * 100);
            holder.progressBar.setProgress(progressPercentage);
            holder.progressTextView.setText(progress + "/" + threshold);
            
            // Затемняем неразблокированные достижения
            holder.itemView.setAlpha(0.7f);
        }
    }
    
    /**
     * ViewHolder для элемента достижения
     */
    static class AchievementViewHolder extends RecyclerView.ViewHolder {
        final ImageView iconImageView;
        final TextView titleTextView;
        final TextView descriptionTextView;
        final ProgressBar progressBar;
        final TextView progressTextView;
        final TextView rewardTextView;
        
        AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.image_achievement);
            titleTextView = itemView.findViewById(R.id.text_achievement_title);
            descriptionTextView = itemView.findViewById(R.id.text_achievement_description);
            progressBar = itemView.findViewById(R.id.progress_achievement);
            progressTextView = itemView.findViewById(R.id.text_achievement_progress);
            rewardTextView = itemView.findViewById(R.id.text_achievement_reward);
        }
    }
}