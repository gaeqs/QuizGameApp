package io.github.gaeqs.quiz.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.stream.Collectors;

import io.github.gaeqs.quiz.data.Answer;
import io.github.gaeqs.quiz.data.Question;

public class DatabaseQuestionWithAnswers {

    @Embedded
    public DatabaseQuestion question;

    @Relation(
            parentColumn = "id",
            entityColumn = "question_id"
    )
    public List<DatabaseAnswer> answers;


    public Question toQuestion() {
        List<Answer> finalAnswers = answers.stream().map(DatabaseAnswer::toAnswer)
                .collect(Collectors.toList());
        return question.toQuestion(finalAnswers);
    }

}
