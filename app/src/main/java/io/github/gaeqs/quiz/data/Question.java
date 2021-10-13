package io.github.gaeqs.quiz.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {

    private final String title;
    private final String image;
    private final List<Answer> answers;
    private final boolean imageQuestion;

    public Question(String title, String image, List<Answer> answers, boolean imageQuestion) {
        this.title = title;
        this.image = image;
        this.answers = Collections.unmodifiableList(new ArrayList<>(answers));
        this.imageQuestion = imageQuestion;
    }

    public String getTitle() {
        return title;
    }

    public String getImage(){
        return image;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public boolean isImageQuestion() {
        return imageQuestion;
    }

    @NonNull
    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", replies=" + answers +
                '}';
    }
}
