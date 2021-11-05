package io.github.gaeqs.quiz.data;

import androidx.annotation.NonNull;

import io.github.gaeqs.quiz.database.DatabaseAnswer;

public class Answer {

    private final String name;
    private final String image;
    private final boolean correct;

    public Answer(String name, String image, boolean correct) {
        this.name = name;
        this.image = image;
        this.correct = correct;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public boolean isCorrect() {
        return correct;
    }

    public DatabaseAnswer toDatabase(String question) {
        return new DatabaseAnswer(question, name, image, correct);
    }

    @NonNull
    @Override
    public String toString() {
        return "Reply{" +
                "name='" + name + '\'' +
                ", image=" + image +
                ", correct=" + correct +
                '}';
    }
}
