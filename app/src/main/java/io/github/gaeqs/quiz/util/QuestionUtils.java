package io.github.gaeqs.quiz.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import io.github.gaeqs.quiz.data.Question;
import io.github.gaeqs.quiz.database.AppDatabase;
import io.github.gaeqs.quiz.database.DatabaseAnswer;
import io.github.gaeqs.quiz.database.DatabaseQuestion;
import io.github.gaeqs.quiz.database.DatabaseQuestionWithAnswers;
import io.github.gaeqs.quiz.game.Difficulty;

public class QuestionUtils {

    private static boolean filled = false;

    public static void fillDatabaseWithQuestions(Context context, int id) throws IOException {
        if (filled) return;
        List<Question> questions = readQuestionsFromJSON(context, id);

        AppDatabase database = AppDatabase.createInstance(context);
        database.questionDao().insertQuestions(questions.stream()
                .map(Question::toDatabase).toArray(DatabaseQuestion[]::new));
        database.answerDao().insertAnswers(questions.stream()
                .flatMap(it -> it.answersToDatabase().stream()).toArray(DatabaseAnswer[]::new));
        filled = true;
    }

    public static List<Question> getQuestionsFromDatabase(
            Context context, Difficulty difficulty, String language) {
        AppDatabase database = AppDatabase.createInstance(context);

        List<DatabaseQuestionWithAnswers> list;
        switch (difficulty) {
            case EASY:
                list = database.questionDao().getEasyQuestionsWithAnswers(language);
                break;
            case HARD:
                list = database.questionDao().getHardQuestionsWithAnswers(language);
                break;
            default:
            case NORMAL:
                list = database.questionDao().getNormalQuestionsWithAnswers(language);
                break;
        }

        return list.stream().map(DatabaseQuestionWithAnswers::toQuestion)
                .collect(Collectors.toList());
    }

    /**
     * Reads the questions stored in the resource that matches the given resource id.
     *
     * @param context the context of the application.
     * @param id      the id of the resource.
     * @return the list of questions.
     * @throws IOException when something bad happens while reading the file.
     */
    public static List<Question> readQuestionsFromJSON(Context context, int id) throws IOException {
        String raw = JSONUtils.readString(context, id);
        Gson gson = new Gson();

        Type listType = new TypeToken<List<Question>>() {
        }.getType();


        return gson.fromJson(raw, listType);
    }

}
