package io.github.gaeqs.quiz.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.gaeqs.quiz.data.User;
import io.github.gaeqs.quiz.data.access.UserDao;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase INSTANCE = null;

    public static AppDatabase createInstance(Context context) {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "app-database").build();
        return INSTANCE;
    }

    public abstract UserDao userDao();
}
