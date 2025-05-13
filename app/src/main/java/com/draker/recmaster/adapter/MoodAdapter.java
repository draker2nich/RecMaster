package com.draker.recmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.draker.recmaster.R;
import com.draker.recmaster.model.Mood;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер для отображения списка настроений
 */
public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MoodViewHolder> {
    
    private List<Mood> moods;
    private final Context context;
    private OnMoodSelectedListener listener;
    private int selectedPosition = -1;
    
    public interface OnMoodSelectedListener {
        void onMoodSelected(Mood mood);
    }
    
    public MoodAdapter(Context context) {
        this.context = context;
        this.moods = new ArrayList<>();
    }
    
    public void setOnMoodSelectedListener(OnMoodSelectedListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public MoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mood, parent, false);
        return new MoodViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MoodViewHolder holder, int position) {
        Mood mood = moods.get(position);
        holder.bind(mood, position, listener);
    }
    
    @Override
    public int getItemCount() {
        return moods != null ? moods.size() : 0;
    }
    
    public void setMoods(List<Mood> moods) {
        this.moods = moods;
        notifyDataSetChanged();
    }
    
    /**
     * Обновление состояния выбранного настроения
     */
    public void selectMood(String moodId) {
        for (int i = 0; i < moods.size(); i++) {
            Mood mood = moods.get(i);
            boolean selected = mood.getId().equals(moodId);
            
            if (mood.isSelected() != selected) {
                mood.setSelected(selected);
                notifyItemChanged(i);
            }
            
            if (selected) {
                selectedPosition = i;
            }
        }
    }
    
    /**
     * Сброс выбора настроения
     */
    public void clearSelection() {
        int prevSelected = selectedPosition;
        selectedPosition = -1;
        
        for (int i = 0; i < moods.size(); i++) {
            if (moods.get(i).isSelected()) {
                moods.get(i).setSelected(false);
                notifyItemChanged(i);
            }
        }
        
        if (prevSelected != -1) {
            notifyItemChanged(prevSelected);
        }
    }
    
    class MoodViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardMood;
        private final ImageView imageMood;
        private final TextView textMoodName;
        
        MoodViewHolder(@NonNull View itemView) {
            super(itemView);
            cardMood = itemView.findViewById(R.id.card_mood);
            imageMood = itemView.findViewById(R.id.image_mood);
            textMoodName = itemView.findViewById(R.id.text_mood_name);
        }
        
        void bind(Mood mood, int position, OnMoodSelectedListener listener) {
            textMoodName.setText(mood.getName());
            imageMood.setImageResource(mood.getIconResId());
            
            // Обновляем внешний вид в зависимости от того, выбрано ли настроение
            cardMood.setStrokeWidth(mood.isSelected() ? 4 : 1);
            cardMood.setCardElevation(mood.isSelected() ? 8 : 2);
            
            itemView.setOnClickListener(v -> {
                int oldSelectedPosition = selectedPosition;
                selectedPosition = position;
                
                // Обновляем состояние всех элементов
                for (int i = 0; i < moods.size(); i++) {
                    moods.get(i).setSelected(i == position);
                }
                
                // Перерисовываем старый и новый выбранные элементы
                if (oldSelectedPosition != -1) {
                    notifyItemChanged(oldSelectedPosition);
                }
                notifyItemChanged(position);
                
                // Уведомляем о выборе настроения
                if (listener != null) {
                    listener.onMoodSelected(mood);
                }
            });
        }
    }
}