package io.github.gaeqs.quiz.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import io.github.gaeqs.quiz.data.Answer;

@Entity(tableName = "answers", primaryKeys = {"question_id", "name"})
public class DatabaseAnswer {

    @NonNull
    @ColumnInfo(name = "question_id")
    private final String questionId;

    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    @ColumnInfo(name = "image")
    private final String image;

    @ColumnInfo(name = "correct")
    private final boolean correct;

    public DatabaseAnswer(@NonNull String questionId, @NonNull String name,
                          String image, boolean correct) {
        this.questionId = questionId;
        this.name = name;
        this.image = image;
        this.correct = correct;
    }

    @NonNull
    public String getQuestionId() {
        return questionId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public boolean isCorrect() {
        return correct;
    }

    public Answer toAnswer () {
        return new Answer(name, image, correct);
    }
}
