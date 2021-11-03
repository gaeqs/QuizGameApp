package io.github.gaeqs.quiz.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.game.Difficulty;
import io.github.gaeqs.quiz.game.QuestionsAmount;

public class ConfigurationActivity extends AppCompatActivity {

    public static final String PREFERENCES = "preferences";
    public static final String PREFERENCES_DIFFICULTY = "difficulty";
    public static final String PREFERENCES_QUESTIONS_AMOUNT = "questions_amount";
    public static final String PREFERENCES_USER = "user";

    private Button buttonDifficulty, buttonQuestions;
    private Difficulty difficulty;
    private QuestionsAmount questionsAmount;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        buttonDifficulty = findViewById(R.id.configuration_difficulty);
        buttonQuestions = findViewById(R.id.configuration_questions);

        SharedPreferences preferences = getSharedPreferences(PREFERENCES, 0);
        editor = preferences.edit();

        difficulty = Difficulty.values()[preferences.getInt(PREFERENCES_DIFFICULTY, 1)];
        questionsAmount = QuestionsAmount.values()
                [preferences.getInt(PREFERENCES_QUESTIONS_AMOUNT, 1)];

        refreshDifficultyButton();
        refreshQuestionsAmountButton();
    }

    public void confirm(View view) {
        editor.apply();
        finish();
    }

    public void createUser(View view) {
        startActivity(new Intent(this, CreateUserActivity.class));
    }

    public void editUser(View view) {
        startActivity(new Intent(this, EditUserActivity.class));
    }

    public void deleteUser(View view) {
        startActivity(new Intent(this, DeleteUserActivity.class));
    }

    public void toggleDifficulty(View view) {
        int ordinal = difficulty.ordinal() + 1;
        if (ordinal >= Difficulty.values().length) ordinal = 0;
        difficulty = Difficulty.values()[ordinal];
        editor.putInt(PREFERENCES_DIFFICULTY, ordinal);
        refreshDifficultyButton();
    }

    public void toggleQuestionsAmount(View view) {
        int ordinal = questionsAmount.ordinal() + 1;
        if (ordinal >= QuestionsAmount.values().length) ordinal = 0;
        questionsAmount = QuestionsAmount.values()[ordinal];
        editor.putInt(PREFERENCES_QUESTIONS_AMOUNT, ordinal);
        refreshQuestionsAmountButton();
    }

    private void refreshDifficultyButton() {
        String difficultyName = getResources().getString(difficulty.getResourceId());
        String message = getResources().getString(R.string.difficulty, difficultyName);
        buttonDifficulty.setText(message);
    }


    private void refreshQuestionsAmountButton() {
        String message = getResources().getString(R.string.questions, questionsAmount.getAmount());
        buttonQuestions.setText(message);
    }

    @Override
    public void onBackPressed() {
        editor.apply();
        super.onBackPressed();
    }
}