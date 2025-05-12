package com.draker.recmaster.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.draker.recmaster.R;
import com.draker.recmaster.data.UserPreferences;
import com.draker.recmaster.model.User;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageProfile;
    private TextInputEditText editUsername;
    private TextInputEditText editEmail;
    private Button buttonChangePhoto;
    private Button buttonSelectGenres;
    private Button buttonSave;
    
    private UserPreferences userPreferences;
    private User currentUser;
    
    private static final int REQUEST_SELECT_GENRES = 101;
    private static final int REQUEST_SELECT_PHOTO = 102;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        
        userPreferences = new UserPreferences(this);
        currentUser = userPreferences.loadUser();
        
        // Инициализация UI
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_profile);
        
        imageProfile = findViewById(R.id.image_profile);
        editUsername = findViewById(R.id.edit_username);
        editEmail = findViewById(R.id.edit_email);
        buttonChangePhoto = findViewById(R.id.button_change_photo);
        buttonSelectGenres = findViewById(R.id.button_select_genres);
        buttonSave = findViewById(R.id.button_save);
        
        // Заполняем поля данными пользователя
        if (currentUser != null) {
            editUsername.setText(currentUser.getUsername());
            editEmail.setText(currentUser.getEmail());
            
            // Если есть аватар, загружаем его
            if (currentUser.getAvatarUri() != null && !currentUser.getAvatarUri().isEmpty()) {
                // Используем Glide для загрузки изображения (не реализовано в данном примере)
            }
        }
        
        // Настраиваем обработчики
        buttonChangePhoto.setOnClickListener(v -> selectPhoto());
        buttonSelectGenres.setOnClickListener(v -> selectGenres());
        buttonSave.setOnClickListener(v -> saveProfile());
    }
    
    private void selectPhoto() {
        // В реальном приложении здесь бы открывался выбор фото из галереи
        // или камеры. Для примера просто показываем сообщение.
        Toast.makeText(this, "Здесь должен быть выбор фото", Toast.LENGTH_SHORT).show();
    }
    
    private void selectGenres() {
        Intent intent = new Intent(this, GenreSelectionActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_GENRES);
    }
    
    private void saveProfile() {
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        
        // Валидация
        if (TextUtils.isEmpty(username)) {
            editUsername.setError(getString(R.string.field_required));
            return;
        }
        
        if (TextUtils.isEmpty(email)) {
            editEmail.setError(getString(R.string.field_required));
            return;
        }
        
        // Обновляем данные пользователя
        currentUser.setUsername(username);
        currentUser.setEmail(email);
        
        // Сохраняем изменения
        userPreferences.saveUser(currentUser);
        
        // Показываем сообщение об успешном сохранении
        Toast.makeText(this, R.string.changes_saved, Toast.LENGTH_SHORT).show();
        
        // Возвращаемся на экран профиля
        setResult(RESULT_OK);
        finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_SELECT_GENRES && resultCode == RESULT_OK && data != null) {
            // Обработка выбранных жанров (будет реализовано в GenreSelectionActivity)
        } else if (requestCode == REQUEST_SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            // Обработка выбранного фото (не реализовано в данном примере)
        }
    }
}