package com.caseyjbrooks.games.features.scorecard;

import com.caseyjbrooks.games.RealmInt;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

public class ScorecardPlayer extends RealmObject {
    @Required
    private String name;
    private int score;

    private RealmList<RealmInt> scoreHistory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public RealmList<RealmInt> getScoreHistory() {
        return scoreHistory;
    }

    public void setScoreHistory(RealmList<RealmInt> scoreHistory) {
        this.scoreHistory = scoreHistory;
    }
}
