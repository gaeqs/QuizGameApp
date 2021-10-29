package io.github.gaeqs.quiz.game;

public enum QuestionsAmount {

    FIVE(5),
    TEN(10),
    FIFTEEN(15);

    private final int amount;

    QuestionsAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
