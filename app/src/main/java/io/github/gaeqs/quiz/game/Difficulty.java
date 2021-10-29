package io.github.gaeqs.quiz.game;

import io.github.gaeqs.quiz.R;

public enum Difficulty {

    EASY(R.string.difficulty_easy),
    NORMAL(R.string.difficulty_normal),
    HARD(R.string.difficulty_hard);

    private final int resourceId;

    Difficulty(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }
}
