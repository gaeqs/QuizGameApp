package io.github.gaeqs.quiz.data.access;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.github.gaeqs.quiz.database.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertUsers(User... users);

    @Update
    void updateUsers(User... users);

    @Query("SELECT * FROM users WHERE name = :name")
    User getUser(String name);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM users WHERE name = :name")
    void deleteUser(String name);


    @Query("DELETE FROM users")
    void deleteAllUsers();
}
