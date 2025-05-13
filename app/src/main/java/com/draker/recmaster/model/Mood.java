package com.draker.recmaster.model;

/**
 * Модель данных для представления настроения пользователя
 */
public class Mood {
    private String id;
    private String name;
    private int iconResId;
    private boolean selected;
    
    public Mood(String id, String name, int iconResId) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
        this.selected = false;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getIconResId() {
        return iconResId;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Mood mood = (Mood) o;
        return id.equals(mood.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}