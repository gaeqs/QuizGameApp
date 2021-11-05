package io.github.gaeqs.quiz.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.gaeqs.quiz.data.Answer;
import io.github.gaeqs.quiz.data.Question;
import io.github.gaeqs.quiz.game.Difficulty;

@Entity(tableName = "questions")
public class DatabaseQuestion {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final String id;

    @NonNull
    @ColumnInfo(name = "language")
    private final String language;

    @NonNull
    @ColumnInfo(name = "title")
    private final String title;

    @ColumnInfo(name = "image")
    private final String image;

    @ColumnInfo(name = "video")
    private final String video;

    @ColumnInfo(name = "audio")
    private final String audio;

    @ColumnInfo(name = "easy")
    private final boolean easy;

    @ColumnInfo(name = "normal")
    private final boolean normal;

    @ColumnInfo(name = "hard")
    private final boolean hard;

    @ColumnInfo(name = "image_question")
    private final boolean imageQuestion;

    public DatabaseQuestion(@NonNull String id, @NonNull String language, @NonNull String title,
                            String image, String video,
                            String audio, boolean easy, boolean normal,
                            boolean hard, boolean imageQuestion) {
        this.id = id;
        this.language = language;
        this.title = title;
        this.image = image;
        this.video = video;
        this.audio = audio;
        this.easy = easy;
        this.normal = normal;
        this.hard = hard;
        this.imageQuestion = imageQuestion;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }

    public String getAudio() {
        return audio;
    }

    public boolean isEasy() {
        return easy;
    }

    public boolean isNormal() {
        return normal;
    }

    public boolean isHard() {
        return hard;
    }

    public boolean isImageQuestion() {
        return imageQuestion;
    }

    public Question toQuestion(List<Answer> answers) {
        Set<Difficulty> difficulties = new HashSet<>(3);
        if (easy) difficulties.add(Difficulty.EASY);
        if (normal) difficulties.add(Difficulty.NORMAL);
        if (hard) difficulties.add(Difficulty.HARD);
        return new Question(id, language, title, image, video, audio, difficulties, answers, imageQuestion);
    }
}
