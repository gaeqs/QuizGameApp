package io.github.gaeqs.quiz.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

import io.github.gaeqs.quiz.util.Validate;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    @ColumnInfo(name = "image")
    private String imagePath;

    @ColumnInfo(name = "matches")
    private int matches;

    @ColumnInfo(name = "maximum_score")
    private int maximumScore;

    @ColumnInfo(name = "maximum_score_time")
    private long maximumScoreTime;

    @ColumnInfo(name = "last_played")
    private long lastPlayed;


    public User(@NonNull String name, String imagePath, int matches, int maximumScore, long maximumScoreTime, long lastPlayed) {
        this.name = name;
        this.imagePath = imagePath;
        this.matches = matches;
        this.maximumScore = maximumScore;
        this.maximumScoreTime = maximumScoreTime;
        this.lastPlayed = lastPlayed;
    }

    @Ignore
    public User(@NonNull String name, String imagePath) {
        Validate.notNull(name, "Name cannot be null!");
        this.name = name;
        this.imagePath = imagePath;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getMaximumScore() {
        return maximumScore;
    }

    public void setMaximumScore(int maximumScore) {
        this.maximumScore = maximumScore;
    }

    public long getMaximumScoreTime() {
        return maximumScoreTime;
    }

    public void setMaximumScoreTime(long maximumScoreTime) {
        this.maximumScoreTime = maximumScoreTime;
    }

    public long getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", matches=" + matches +
                ", maximumScore=" + maximumScore +
                ", lastPlayed=" + lastPlayed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
