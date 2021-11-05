package io.github.gaeqs.quiz.data.access;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.github.gaeqs.quiz.database.DatabaseQuestion;
import io.github.gaeqs.quiz.database.DatabaseQuestionWithAnswers;

@Dao
public interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertQuestions(DatabaseQuestion... questions);

    @Transaction
    @Query("SELECT * FROM questions")
    List<DatabaseQuestionWithAnswers> getQuestionsWithAnswers();

    @Transaction
    @Query("SELECT * FROM questions WHERE easy = 1 AND language = :language")
    List<DatabaseQuestionWithAnswers> getEasyQuestionsWithAnswers(String language);

    @Transaction
    @Query("SELECT * FROM questions WHERE normal = 1 AND language = :language")
    List<DatabaseQuestionWithAnswers> getNormalQuestionsWithAnswers(String language);

    @Transaction
    @Query("SELECT * FROM questions WHERE hard = 1 AND language = :language")
    List<DatabaseQuestionWithAnswers> getHardQuestionsWithAnswers(String language);
}
