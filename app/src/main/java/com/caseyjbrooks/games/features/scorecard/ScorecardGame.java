package com.caseyjbrooks.games.features.scorecard;

import com.caseyjbrooks.games.RealmInt;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ScorecardGame extends RealmObject {
    @Required
    private String name;

    @Required
    @PrimaryKey

    private String key;

    @Required
    private Date dateCreated;

    @Required
    private Date dateModified;

    private RealmList<ScorecardPlayer> players;
    private RealmList<RealmInt> buttonValues;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public RealmList<ScorecardPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(RealmList<ScorecardPlayer> players) {
        this.players = players;
    }

    public RealmList<RealmInt> getButtonValues() {
        return buttonValues;
    }

    public void setButtonValues(RealmList<RealmInt> buttonValues) {
        this.buttonValues = buttonValues;
    }
}
