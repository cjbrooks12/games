package com.caseyjbrooks.games.features.scorecard;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class ScorecardAction extends RealmObject {
    @Required
    private String player;

    @Required
    private String scoreAction;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getScoreAction() {
        return scoreAction;
    }

    public void setScoreAction(String scoreAction) {
        this.scoreAction = scoreAction;
    }
}
