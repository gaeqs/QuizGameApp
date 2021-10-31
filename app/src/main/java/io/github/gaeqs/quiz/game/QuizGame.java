package io.github.gaeqs.quiz.game;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import io.github.gaeqs.quiz.ConfigurationActivity;
import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.data.Question;
import io.github.gaeqs.quiz.util.JSONUtils;
import io.github.gaeqs.quiz.util.Validate;

public class QuizGame {

    public static QuizGame GAME = null;

    public static void startNewGame(Context context, String username) {
        List<Question> questions;
        try {
            questions = JSONUtils.readQuestionsFromJSON(context, R.raw.quizzes);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        SharedPreferences preferences =
                context.getSharedPreferences(ConfigurationActivity.PREFERENCES, 0);

        // Difficulty still not being used!
        Difficulty difficulty = Difficulty.values()
                [preferences.getInt(ConfigurationActivity.PREFERENCES_DIFFICULTY, 1)];

        QuestionsAmount amount = QuestionsAmount.values()
                [preferences.getInt(ConfigurationActivity.PREFERENCES_QUESTIONS_AMOUNT, 1)];

        GAME = new QuizGame(questions, username, amount.getAmount());
    }

    private final String username;

    private final List<Question> questions;
    private final LinkedList<Question> unusedQuestions;
    private final List<BiConsumer<QuizGame, QuizGameStatus>> statusChangeEvent;
    private final int maxQuestions;

    private Question currentQuestion;
    private QuizGameStatus status;
    private int score;
    private int answeredQuestions;
    private int selectedAnswer;
    private boolean correct;
    private int correctAnswers;
    private int wrongAnswers;

    private long initTime;
    private long finishTime;


    public QuizGame(List<Question> questions, String username, int maxQuestions) {
        Validate.notNull(questions, "Questions cannot be null!");
        Validate.isTrue(!questions.isEmpty(), "Questions cannot be empty!");
        this.username = username;
        this.questions = questions;
        this.maxQuestions = maxQuestions;
        this.unusedQuestions = new LinkedList<>(questions);
        Collections.shuffle(unusedQuestions);

        this.currentQuestion = unusedQuestions.pop();
        this.status = QuizGameStatus.ANSWERING;
        this.statusChangeEvent = new LinkedList<>();

        this.initTime = System.currentTimeMillis();
        this.finishTime = 0;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public QuizGameStatus getStatus() {
        return status;
    }

    public int getScore() {
        return score;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public int getSelectedAnswer() {
        return selectedAnswer;
    }

    public int getAnsweredQuestions() {
        return answeredQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public boolean isAnswerCorrect() {
        return correct;
    }

    public int getMaxQuestions() {
        return maxQuestions;
    }

    public void addChangeListener(BiConsumer<QuizGame, QuizGameStatus> listener) {
        Validate.notNull(listener, "Listener cannot be null!");
        statusChangeEvent.add(listener);
    }

    public void removeChangeListener(BiConsumer<QuizGame, QuizGameStatus> listener) {
        Validate.notNull(listener, "Listener cannot be null!");
        statusChangeEvent.remove(listener);
    }

    public void answer(int answer) {
        if (status != QuizGameStatus.ANSWERING) return;
        this.selectedAnswer = answer;
        correct = currentQuestion.getAnswers().get(answer).isCorrect();

        if (correct) {
            correctAnswers++;
        } else {
            wrongAnswers++;
        }

        score += correct ? 3 : -2;
        answeredQuestions++;
        changeStatus(QuizGameStatus.ANSWERED);
        nextQuestion();
    }

    public void nextQuestion() {
        if (status != QuizGameStatus.ANSWERED) return;

        if (answeredQuestions >= maxQuestions) {
            // FINISH!
            finishTime = System.currentTimeMillis() - initTime;
            changeStatus(QuizGameStatus.FINISHED);
            return;
        }

        if (unusedQuestions.isEmpty()) {
            unusedQuestions.addAll(questions);
            Collections.shuffle(unusedQuestions);
            unusedQuestions.remove(currentQuestion);
        }

        this.currentQuestion = unusedQuestions.pop();
        changeStatus(QuizGameStatus.ANSWERING);
    }

    private void changeStatus(QuizGameStatus status) {
        this.status = status;
        statusChangeEvent.forEach(it -> it.accept(this, status));
    }

    public long getFinishTime() {
        return finishTime;
    }
}
