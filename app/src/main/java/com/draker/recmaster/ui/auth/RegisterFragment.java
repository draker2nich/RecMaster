package com.draker.recmaster.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.draker.recmaster.R;
import com.draker.recmaster.data.UserPreferences;
import com.draker.recmaster.model.User;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterFragment extends Fragment {

    private TextInputEditText editUsername;
    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private TextInputEditText editPasswordConfirm;
    private Button btnRegister;
    private UserPreferences userPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        
        userPreferences = new UserPreferences(requireContext());
        
        // Инициализация UI
        editUsername = view.findViewById(R.id.edit_username);
        editEmail = view.findViewById(R.id.edit_email);
        editPassword = view.findViewById(R.id.edit_password);
        editPasswordConfirm = view.findViewById(R.id.edit_password_confirm);
        btnRegister = view.findViewById(R.id.button_register);
        
        btnRegister.setOnClickListener(v -> registerUser());
        
        return view;
    }
    
    private void registerUser() {
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String passwordConfirm = editPasswordConfirm.getText().toString().trim();
        
        // Валидация полей
        if (TextUtils.isEmpty(username)) {
            editUsername.setError(getString(R.string.field_required));
            return;
        }
        
        if (TextUtils.isEmpty(email)) {
            editEmail.setError(getString(R.string.field_required));
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            editPassword.setError(getString(R.string.field_required));
            return;
        }
        
        if (TextUtils.isEmpty(passwordConfirm)) {
            editPasswordConfirm.setError(getString(R.string.field_required));
            return;
        }
        
        if (!password.equals(passwordConfirm)) {
            editPasswordConfirm.setError(getString(R.string.passwords_dont_match));
            return;
        }
        
        // Создаем нового пользователя
        User newUser = new User(username, email, password);
        
        // Сохраняем пользователя
        userPreferences.saveUser(newUser);
        
        // Показываем сообщение об успешной регистрации
        Toast.makeText(requireContext(), R.string.register_success, Toast.LENGTH_SHORT).show();
        
        // Переходим на главный экран
        ((LoginActivity) requireActivity()).startMainActivity();
    }
}