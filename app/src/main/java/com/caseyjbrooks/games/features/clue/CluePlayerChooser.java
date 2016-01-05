package com.caseyjbrooks.games.features.clue;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.caseyjbrooks.games.R;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

public class CluePlayerChooser extends LinearLayout {

    View[] players = new View[6];

    public CluePlayerChooser(Context context) {
        super(context);
        init(null, 0);
    }

    public CluePlayerChooser(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CluePlayerChooser(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        LayoutInflater.from(getContext()).inflate(R.layout.clue_player_chooser, this);

        players[0] = findViewById(R.id.layout_p0);
        players[1] = findViewById(R.id.layout_p1);
        players[2] = findViewById(R.id.layout_p2);
        players[3] = findViewById(R.id.layout_p3);
        players[4] = findViewById(R.id.layout_p4);
        players[5] = findViewById(R.id.layout_p5);

        DragLinearLayout dragLinearLayout = (DragLinearLayout) findViewById(R.id.container);

        dragLinearLayout.setViewDraggable(players[0], players[0].findViewById(R.id.handle_p0));
        dragLinearLayout.setViewDraggable(players[1], players[1].findViewById(R.id.handle_p1));
        dragLinearLayout.setViewDraggable(players[2], players[2].findViewById(R.id.handle_p2));
        dragLinearLayout.setViewDraggable(players[3], players[3].findViewById(R.id.handle_p3));
        dragLinearLayout.setViewDraggable(players[4], players[4].findViewById(R.id.handle_p4));
        dragLinearLayout.setViewDraggable(players[5], players[5].findViewById(R.id.handle_p5));
    }
}
