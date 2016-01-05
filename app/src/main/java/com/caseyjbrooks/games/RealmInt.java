package com.caseyjbrooks.games;

import io.realm.RealmObject;

public class RealmInt extends RealmObject {
    private int value;

    public RealmInt() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
