package io.github.gaeqs.quiz.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import io.github.gaeqs.quiz.game.Difficulty;

public class Question {

    private final String title;
    private final String image;
    private final String video;
    private final String audio;
    private final Set<Difficulty> difficulties;
    private final List<Answer> answers;
    private final boolean imageQuestion;

    public Question(String title, String image, String video,
                    String audio, Set<Difficulty> difficulties, List<Answer> answers, boolean imageQuestion) {
        this.title = title;
        this.image = image;
        this.video = video;
        this.audio = audio;
        this.difficulties = difficulties;
        this.answers = Collections.unmodifiableList(new ArrayList<>(answers));
        this.imageQuestion = imageQuestion;
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
        return difficulties;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public boolean isImageQuestion() {
        return imageQuestion;
    }

    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", video='" + video + '\'' +
                ", audio='" + audio + '\'' +
                ", difficulties=" + difficulties +
                ", answers=" + answers +
                ", imageQuestion=" + imageQuestion +
                '}';
    }
}
