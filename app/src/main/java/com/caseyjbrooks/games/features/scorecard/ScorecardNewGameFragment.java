package com.caseyjbrooks.games.features.scorecard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.caseyjbrooks.games.R;
import com.caseyjbrooks.games.RealmInt;
import com.caseyjbrooks.games.Util;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

public class ScorecardNewGameFragment extends Fragment {

    EditText nameInput;
    SeekBar playerCountSeekbar;
    TextView playerCountTextView;
    LinearLayout playerNamesLayout;

    public static Fragment newInstance() {
        ScorecardNewGameFragment fragment = new ScorecardNewGameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scorecard_new_game, container, false);

        nameInput = (EditText) view.findViewById(R.id.nameInput);
        view.findViewById(R.id.createGameButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGame();
            }
        });

        playerCountTextView = (TextView) view.findViewById(R.id.playerCountTextView);
        playerCountSeekbar = (SeekBar) view.findViewById(R.id.playerCountSeekbar);
        playerNamesLayout = (LinearLayout) view.findViewById(R.id.playerNamesLayout);
        playerCountSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePlayerCounts();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        updatePlayerCounts();

        return view;
    }

    private void updatePlayerCounts() {
        int players = playerCountSeekbar.getProgress();
        playerCountTextView.setText(players + " players in game");

        playerNamesLayout.removeAllViews();
        for(int i = 1; i <= players; i++) {
            EditText playerName = new EditText(getContext());
            playerName.setHint("Player " + i);
            playerNamesLayout.addView(playerName);
        }
    }

    public void createGame() {
        Realm.getInstance(getContext()).executeTransaction(
        new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                ScorecardGame game = bgRealm.createObject(ScorecardGame.class);

                if (nameInput.getText().length() > 0)
                    game.setName(nameInput.getText().toString());
                else {
                    game.setName("Game " + Util.randomString(8));
                }

                Date date = new Date(Calendar.getInstance().getTimeInMillis());

                game.setDateCreated(date);
                game.setDateModified(date);
                game.setKey(Util.randomString(24));

                for(int i = 0; i < playerNamesLayout.getChildCount(); i++) {
                    EditText playerNames = (EditText) playerNamesLayout.getChildAt(i);

                    ScorecardPlayer newPlayer = bgRealm.createObject(ScorecardPlayer.class);
                    newPlayer.setScore(0);

                    if(playerNames.getText().length() == 0) {
                        newPlayer.setName("Player " + (i+1));
                    }
                    else {
                        newPlayer.setName(playerNames.getText().toString());
                    }

//                    for(int j = 0; j < 50; j++) {
//                        RealmInt historyValue = bgRealm.createObject(RealmInt.class);
//                        historyValue.setValue(j);
//                        newPlayer.getScoreHistory().add(historyValue);
//                    }

                    game.getPlayers().add(newPlayer);
                }

                for(int i = 0; i < 5; i++) {
                    RealmInt buttonValue = bgRealm.createObject(RealmInt.class);
                    buttonValue.setValue(i);
                    game.getButtonValues().add(buttonValue);
                }
            }
        },
        new Realm.Transaction.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Game saved successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "Error Saving Game", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
