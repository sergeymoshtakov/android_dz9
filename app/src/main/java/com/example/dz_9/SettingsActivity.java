package com.example.dz_9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private RadioGroup formatRadioGroup;
    private RadioButton txtFormatRadioButton, jsonFormatRadioButton;
    private Button saveSettingsButton, backToMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        formatRadioGroup = findViewById(R.id.formatRadioGroup);
        txtFormatRadioButton = findViewById(R.id.txtFormatRadioButton);
        jsonFormatRadioButton = findViewById(R.id.jsonFormatRadioButton);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);
        backToMainButton = findViewById(R.id.backToMainButton);

        // Загружаем настройки
        SharedPreferences preferences = getSharedPreferences("GameSettings", MODE_PRIVATE);
        String fileFormat = preferences.getString("fileFormat", "TXT");

        // Устанавливаем радиокнопку в соответствии с сохраненным форматом
        if ("TXT".equals(fileFormat)) {
            txtFormatRadioButton.setChecked(true);
        } else {
            jsonFormatRadioButton.setChecked(true);
        }

        // Сохранение настроек
        saveSettingsButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("fileFormat", txtFormatRadioButton.isChecked() ? "TXT" : "JSON");
            editor.apply();
        });

        // Переход на главную страницу
        backToMainButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
