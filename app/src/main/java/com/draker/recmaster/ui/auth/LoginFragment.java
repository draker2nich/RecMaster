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

public class LoginFragment extends Fragment {

    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private Button btnLogin;
    private UserPreferences userPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        
        userPreferences = new UserPreferences(requireContext());
        
        // Инициализация UI
        editEmail = view.findViewById(R.id.edit_email);
        editPassword = view.findViewById(R.id.edit_password);
        btnLogin = view.findViewById(R.id.button_login);
        
        btnLogin.setOnClickListener(v -> loginUser());
        
        return view;
    }
    
    private void loginUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        
        // Простая валидация полей
        if (TextUtils.isEmpty(email)) {
            editEmail.setError(getString(R.string.field_required));
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            editPassword.setError(getString(R.string.field_required));
            return;
        }
        
        // Проверяем данные в SharedPreferences
        User savedUser = userPreferences.loadUser();
        if (savedUser != null && savedUser.getEmail().equals(email) && savedUser.getPassword().equals(password)) {
            // Успешный вход
            ((LoginActivity) requireActivity()).startMainActivity();
        } else {
            // Неверные данные
            Toast.makeText(requireContext(), R.string.login_error, Toast.LENGTH_SHORT).show();
        }
    }
}