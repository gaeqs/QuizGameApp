package io.github.gaeqs.quiz.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import io.github.gaeqs.quiz.util.Validate;

public class UserStorage extends AndroidViewModel {

    private final LiveData<List<User>> users;

    public UserStorage(Application application) {
        super(application);
        AppDatabase.createInstance(application);
        users = AppDatabase.INSTANCE.userDao().getAllUsers();
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public Future<Optional<User>> getUser(String name) {
        return AppDatabase.databaseWriteExecutor.submit(() ->
                Optional.ofNullable(AppDatabase.INSTANCE.userDao().getUser(name)));
    }

    public boolean addNewUser(User user) {
        Validate.notNull(user, "User cannot be null!");

        List<User> list = users.getValue();
        if (list != null && list.stream().anyMatch(it -> it.getName().equals(user.getName())))
            return false;

        AppDatabase.databaseWriteExecutor.execute(() ->
                AppDatabase.INSTANCE.userDao().insertUsers(user));
        return true;
    }

    public boolean removeUser(String name) {
        Validate.notNull(name, "Name cannot be null!");
        AppDatabase.databaseWriteExecutor.execute(() ->
                AppDatabase.INSTANCE.userDao().deleteUser(name));
        return true;
    }

    public void removeAllUsers() {
        AppDatabase.databaseWriteExecutor.execute(() ->
                AppDatabase.INSTANCE.userDao().deleteAllUsers());
    }
}
