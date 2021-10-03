package io.github.gaeqs.quiz.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {

    private final String title;
    private final List<Answer> answers;

    public Question(String title, List<Answer> answers) {
        this.title = title;
        this.answers = Collections.unmodifiableList(new ArrayList<>(answers));
    }

    public String getTitle() {
        return title;
    }

    public List<Answer> getAnswers() {
        return answers;
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
