package com.example.dz_9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView questionText, resultText;
    private EditText answerInput;
    private Button checkAnswerButton, saveScoreButton, settingsButton;

    private final String[] questions = {
            "Розв'яжіть: 5 + 3 = ?",
            "Розв'яжіть: 10 - 7 = ?",
            "Розв'яжіть: 6 * 2 = ?"
    };

    private final int[] answers = {8, 3, 12}; // Правильные ответы
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionText = findViewById(R.id.questionText);
        answerInput = findViewById(R.id.answerInput);
        checkAnswerButton = findViewById(R.id.checkAnswerButton);
        resultText = findViewById(R.id.resultText);
        saveScoreButton = findViewById(R.id.saveScoreButton);
        settingsButton = findViewById(R.id.settingsButton);

        // Отображаем первый вопрос
        displayQuestion();

        // Проверка ответа
        checkAnswerButton.setOnClickListener(v -> checkAnswer());

        // Сохранение результата
        saveScoreButton.setOnClickListener(v -> saveScore());

        // Переход к настройкам
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.length) {
            questionText.setText(questions[currentQuestionIndex]);
            answerInput.setText(""); // Очищаем поле ввода для нового вопроса
            resultText.setText(""); // Сбрасываем результат для нового вопроса
        } else {
            // Завершение всех вопросов
            resultText.setText("Вы ответили правильно на " + correctAnswersCount + " из " + questions.length + " вопросов.");
            checkAnswerButton.setEnabled(false); // Деактивируем кнопку проверки ответа
        }
    }

    private void checkAnswer() {
        String userAnswerText = answerInput.getText().toString();
        if (userAnswerText.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, введите ответ", Toast.LENGTH_SHORT).show();
            return;
        }

        int userAnswer = Integer.parseInt(userAnswerText);
        if (userAnswer == answers[currentQuestionIndex]) {
            resultText.setText("Правильно!");
            correctAnswersCount++;
        } else {
            resultText.setText("Неправильно! Попробуйте еще раз.");
        }

        // Переходим к следующему вопросу
        currentQuestionIndex++;
        displayQuestion();
    }

    private void saveScore() {
        // Получаем формат сохранения из настроек
        SharedPreferences preferences = getSharedPreferences("GameSettings", MODE_PRIVATE);
        String fileFormat = preferences.getString("fileFormat", "TXT");

        // Формируем строку с результатом
        String resultData = "Правильные ответы: " + correctAnswersCount + " из " + questions.length;

        // Сохранение результата в нужном формате
        try {
            FileOutputStream fos;
            if ("JSON".equals(fileFormat)) {
                fos = openFileOutput("score.json", MODE_PRIVATE);
                resultData = "{ \"correctAnswers\": " + correctAnswersCount + ", \"totalQuestions\": " + questions.length + " }";
            } else {
                fos = openFileOutput("score.txt", MODE_PRIVATE);
            }
            fos.write(resultData.getBytes());
            fos.close();
            Toast.makeText(this, "Результат сохранен в формате " + fileFormat + "!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка сохранения результата", Toast.LENGTH_SHORT).show();
        }
    }
}