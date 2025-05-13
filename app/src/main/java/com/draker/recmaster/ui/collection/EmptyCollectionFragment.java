package com.draker.recmaster.ui.collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.draker.recmaster.R;

/**
 * Фрагмент для отображения пустой коллекции
 */
public class EmptyCollectionFragment extends Fragment {

    private final String message;

    public EmptyCollectionFragment(String message) {
        this.message = message;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empty_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textMessage = view.findViewById(R.id.text_empty_message);
        textMessage.setText(message);
    }
}