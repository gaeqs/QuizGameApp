package io.github.gaeqs.quiz.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.gaeqs.quiz.database.DatabaseAnswer;
import io.github.gaeqs.quiz.database.DatabaseQuestion;
import io.github.gaeqs.quiz.game.Difficulty;

public class Question {

    private final String id;
    private final String language;
    private final String title;
    private final String image;
    private final String video;
    private final String audio;
    private final Set<Difficulty> difficulty;
    private final List<Answer> answers;
    private final boolean imageQuestion;

    public Question(String id, String language, String title, String image, String video,
                    String audio, Set<Difficulty> difficulties, List<Answer> answers, boolean imageQuestion) {
        this.id = id;
        this.language = language;
        this.title = title;
        this.image = image;
        this.video = video;
        this.audio = audio;
        this.difficulty = Collections.unmodifiableSet(new HashSet<>(difficulties));
        this.answers = Collections.unmodifiableList(new ArrayList<>(answers));
        this.imageQuestion = imageQuestion;
    }

    public String getId() {
        return id;
    }

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

    public Set<Difficulty> getDifficulties() {
        return difficulty;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public boolean isImageQuestion() {
        return imageQuestion;
    }

    public DatabaseQuestion toDatabase() {
        return new DatabaseQuestion(id, language, title, image, video, audio,
                difficulty.contains(Difficulty.EASY),
                difficulty.contains(Difficulty.NORMAL),
                difficulty.contains(Difficulty.HARD),
                imageQuestion);
    }

    public List<DatabaseAnswer> answersToDatabase() {
        return answers.stream().map(it -> it.toDatabase(id)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", video='" + video + '\'' +
                ", audio='" + audio + '\'' +
                ", difficulty=" + difficulty +
                ", answers=" + answers +
                ", imageQuestion=" + imageQuestion +
                '}';
    }
}
