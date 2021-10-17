package io.github.gaeqs.quiz.game;

import android.content.Context;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.data.Question;
import io.github.gaeqs.quiz.util.JSONUtils;
import io.github.gaeqs.quiz.util.Validate;

public class QuizGame {

    public static QuizGame GAME = null;

    public static void startNewGame(Context context) {
        List<Question> questions;
        try {
            questions = JSONUtils.readQuestionsFromJSON(context, R.raw.quizzes);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        GAME = new QuizGame(questions, 5);
    }

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


    public QuizGame(List<Question> questions, int maxQuestions) {
        Validate.notNull(questions, "Questions cannot be null!");
        Validate.isTrue(!questions.isEmpty(), "Questions cannot be empty!");
        this.questions = questions;
        this.maxQuestions = maxQuestions;
        this.unusedQuestions = new LinkedList<>(questions);
        Collections.shuffle(unusedQuestions);

        this.currentQuestion = unusedQuestions.pop();
        this.status = QuizGameStatus.ANSWERING;
        this.statusChangeEvent = new LinkedList<>();
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

    public boolean isAnswerCorrect() {
        return correct;
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
        score += correct ? 3 : -2;
        answeredQuestions++;
        changeStatus(QuizGameStatus.ANSWERED);
    }

    public void nextQuestion() {
        if (status != QuizGameStatus.ANSWERED) return;

        if (answeredQuestions >= maxQuestions) {
            // FINISH!
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
}
