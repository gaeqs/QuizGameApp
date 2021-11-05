package io.github.gaeqs.quiz.data.access;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import io.github.gaeqs.quiz.database.DatabaseAnswer;

@Dao
public interface AnswerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAnswers(DatabaseAnswer... answers);
}
