package com.caseyjbrooks.games.features.scorecard;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caseyjbrooks.games.R;
import com.caseyjbrooks.games.RealmInt;
import com.caseyjbrooks.games.Util;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

public class ScorecardActivity extends AppCompatActivity {
    ScorecardGame game;

    Handler saveScoresHandler = new Handler();

    RecyclerView scorecardPlayersRecyclerView;
    GameAdapter gameAdapter;
    FloatingActionButton addPlayersButton;
    CoordinatorLayout coordinatorLayout;

//Initialization
//--------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);

        setupGame();
        setupToolbar();
        setupButtons();
        setupPlayers();
    }

    public void setupGame() {
        Bundle extra = getIntent().getExtras();
        String key = (extra != null) ? extra.getString("GAME_ID") : "";

        Realm realm = Realm.getInstance(this);
        game = realm.where(ScorecardGame.class).equalTo("key", key).findFirst();
    }

    public void setupToolbar() {
        coordinatorLayout  = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(game.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.fab).setOnClickListener(fabClick);
    }

    public void setupButtons() {
        LinearLayout posButtonLayout = (LinearLayout) findViewById(R.id.posButtonLayout);
        posButtonLayout.setWeightSum(game.getButtonValues().size() + 1);
        LinearLayout negButtonLayout = (LinearLayout) findViewById(R.id.negButtonLayout);
        negButtonLayout.setWeightSum(game.getButtonValues().size() + 1);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f);

        for(int i = 0; i <= game.getButtonValues().size(); i++) {
            for(int j = 0; j < 2; j++) {
                String text = "";

                if(i == game.getButtonValues().size()) {
                    text = (j == 0) ? "..." : "-(...)";
                }
                else {
                    text += ((j == 0) ? "" : "-") + game.getButtonValues().get(i).getValue();
                }

                Button button = new Button(this);
                button.setText(text);
                button.setLayoutParams(params);
                button.setOnClickListener(scoreButtonClicked);

                if(j == 0) {
                    posButtonLayout.addView(button);
                }
                else {
                    negButtonLayout.addView(button);
                }
            }
        }
    }

    public void setupPlayers() {
        scorecardPlayersRecyclerView = (RecyclerView) findViewById(R.id.scorecardPlayersListview);
        scorecardPlayersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scorecardPlayersRecyclerView.setLayoutManager(layoutManager);

        gameAdapter = new GameAdapter(this, game.getPlayers());
        scorecardPlayersRecyclerView.setAdapter(gameAdapter);
    }

//Menu
//--------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scorecard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scorecard_settings:
                AlertDialog.Builder builder = new AlertDialog.Builder(ScorecardActivity.this);
                builder.setCancelable(false);
                builder.setNegativeButton("Close", new Util.DismissDialogCallback());
                builder.setTitle("Game Settings");
                builder.setMessage("Settings, settings, settings...");
                builder.create().show();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

//floating action button
//--------------------------------------------------------------------------------------------------
    private View.OnClickListener fabClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(coordinatorLayout, "Add new player", Snackbar.LENGTH_LONG).show();
        }
    };


//Onclick listeners
//--------------------------------------------------------------------------------------------------
    private View.OnClickListener scoreButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;

            if(button.getText().toString().contains("...")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ScorecardActivity.this);
                builder.setCancelable(false);
                builder.setNegativeButton("Close", new Util.DismissDialogCallback());

                View view = LayoutInflater.from(ScorecardActivity.this).inflate(R.layout.dialog_edittext, null);
                final EditText editText = (EditText) view.findViewById(R.id.dialog_edittext);
                builder.setView(view);

                if(button.getText().toString().equals("...")) {
                    builder.setTitle("Add custom amount");
                    builder.setMessage("Add some custom amount to the selected items");

                    builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(editText.getText().length() > 0) {
                                addScoreToSelectedPlayers(Integer.parseInt(editText.getText().toString()));
                            }

                            dialog.dismiss();
                        }
                    });
                }
                else {
                    builder.setTitle("Subtract custom amount");
                    builder.setMessage("Subtract some custom amount to the selected items");

                    builder.setPositiveButton("Subtract", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(editText.getText().length() > 0) {
                                addScoreToSelectedPlayers(-1*Integer.parseInt(editText.getText().toString()));
                            }

                            dialog.dismiss();
                        }
                    });
                }

                builder.create().show();
            } else {
                addScoreToSelectedPlayers(Integer.parseInt(button.getText().toString()));
            }
        }
    };

    public void addScoreToSelectedPlayers(int value) {
        for (int i = 0; i < gameAdapter.getMetadata().size(); i++) {
            final Bundle playerData = gameAdapter.getMetadata().get(i);

            if (playerData.getInt("STATE") == PlayerState.Selected.ordinal()) {
                playerData.putInt("TEMP_CHANGE", playerData.getInt("TEMP_CHANGE", 0) + value);

                saveScoresHandler.removeCallbacks(saveTempScoresRunnable);
                saveScoresHandler.postDelayed(saveTempScoresRunnable, 5000);
            }
        }
        gameAdapter.notifyDataSetChanged();
    }

    Runnable saveTempScoresRunnable = new Runnable() {
        @Override
        public void run() {
            for(int i = 0; i < gameAdapter.getMetadata().size(); i++) {
                final ScorecardPlayer player = gameAdapter.getPlayers().get(i);
                final Bundle playerData = gameAdapter.getMetadata().get(i);

                Realm realm = Realm.getInstance(ScorecardActivity.this);
                if(playerData.getInt("TEMP_CHANGE", 0) != 0) {
                    realm.beginTransaction();
                    RealmInt history = realm.createObject(RealmInt.class);
                    history.setValue(playerData.getInt("TEMP_CHANGE", 0));
                    player.setScore(player.getScore() + history.getValue());
                    player.getScoreHistory().add(history);
                    realm.commitTransaction();

                    playerData.putInt("STATE", PlayerState.UnSelected.ordinal());
                    playerData.putInt("TEMP_CHANGE", 0);
                }
            }
            gameAdapter.notifyDataSetChanged();
        }
    };

//List of players
//--------------------------------------------------------------------------------------------------
    private enum PlayerState {
        UnSelected, Selected
    }

    private class GamePlayerViewHolder extends RecyclerView.ViewHolder {
        ScorecardPlayer data;
        Bundle metadata;

        View vRoot;
        TextView vName;
        TextView vScore;
        RecyclerView vHistory;

        public GamePlayerViewHolder(View itemView) {
            super(itemView);

            vRoot =  itemView.findViewById(R.id.root);
            vName = (TextView) itemView.findViewById(R.id.name);
            vScore = (TextView) itemView.findViewById(R.id.score);
            vHistory = (RecyclerView) itemView.findViewById(R.id.history);
            LinearLayoutManager layoutManager = new LinearLayoutManager(ScorecardActivity.this, LinearLayoutManager.HORIZONTAL, false);
            vHistory.setLayoutManager(layoutManager);
        }

        public void bind(final ScorecardPlayer data, final Bundle metadata) {
            this.data = data;
            this.metadata = metadata;

            vName.setText(data.getName());

            //change color of text and enable click if there is a temp change not yet committed
            if (metadata.getInt("TEMP_CHANGE", 0) != 0) {
                vScore.setTextColor(Util.getThemeColor(ScorecardActivity.this, R.attr.colorAccentSelected));
                vScore.setText(metadata.getInt("TEMP_CHANGE", 0) + "");
                vScore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("score clicked", vName.getText().toString() + "'s score has been clicked");

                        Realm realm = Realm.getInstance(ScorecardActivity.this);
                        realm.beginTransaction();
                        RealmInt history = realm.createObject(RealmInt.class);
                        history.setValue(metadata.getInt("TEMP_CHANGE", 0));
                        data.setScore(data.getScore() + history.getValue());
                        data.getScoreHistory().add(history);
                        realm.commitTransaction();

                        metadata.putInt("STATE", PlayerState.UnSelected.ordinal());
                        metadata.putInt("TEMP_CHANGE", 0);
                        gameAdapter.notifyDataSetChanged();
                    }
                });
                vScore.setClickable(true);
                vScore.setEnabled(true);
            } else {
                vScore.setTextColor(Util.getThemeColor(ScorecardActivity.this, android.R.attr.textColorPrimary));
                vScore.setText(data.getScore() + "");
                vScore.setOnClickListener(null);
                vScore.setClickable(false);
                vScore.setEnabled(false);
            }

            //change background color to indicate an item is selected
            if(metadata.getInt("STATE") == PlayerState.Selected.ordinal()) {
                vRoot.setSelected(true);
            }
            else {
                vRoot.setSelected(false);
            }

            vRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("root clicked", vName.getText().toString() + "'s root has been clicked");

                    if (metadata.getInt("STATE") == PlayerState.UnSelected.ordinal()) {
                        metadata.putInt("STATE", PlayerState.Selected.ordinal());
                    } else {
                        metadata.putInt("STATE", PlayerState.UnSelected.ordinal());
                    }

                    gameAdapter.notifyDataSetChanged();
                }
            });

            //set history
            PlayerHistoryAdapter playerHistoryAdapter = new PlayerHistoryAdapter(ScorecardActivity.this, data.getScoreHistory());
            vHistory.setAdapter(playerHistoryAdapter);
        }
    }

    private class GameAdapter extends RecyclerView.Adapter<GamePlayerViewHolder>  {
        RealmList<ScorecardPlayer> players;
        ArrayList<Bundle> metadata;
        Context context;

        public GameAdapter(Context context, RealmList<ScorecardPlayer> realmPlayers) {
            this.context = context;
            this.players = realmPlayers;
            this.metadata = new ArrayList<>();

            for(int i = 0; i < players.size(); i++) {
                metadata.add(new Bundle());
                metadata.get(i).putInt("STATE", PlayerState.UnSelected.ordinal());
            }
        }

        @Override
        public int getItemCount() {
            return players.size();
        }

        @Override
        public void onBindViewHolder(GamePlayerViewHolder contactViewHolder, int i) {
            contactViewHolder.bind(players.get(i), metadata.get(i));
        }

        @Override
        public GamePlayerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scorecard_game_player_item, viewGroup, false);

            return new GamePlayerViewHolder(itemView);
        }

        public ArrayList<Bundle> getMetadata() {
            return metadata;
        }

        public RealmList<ScorecardPlayer> getPlayers() {
            return players;
        }
    }

//List of history items for each player
//--------------------------------------------------------------------------------------------------
    private static class PlayerHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView vScore;

        public PlayerHistoryViewHolder(View itemView) {
            super(itemView);
            vScore = (TextView) itemView.findViewById(R.id.score);
        }

        public void bind(final RealmInt data) {

            vScore.setText(Integer.toString(data.getValue()));
        }
    }

    private static class PlayerHistoryAdapter extends RecyclerView.Adapter<PlayerHistoryViewHolder>  {
        RealmList<RealmInt> history;
        ArrayList<Bundle> metadata;
        Context context;

        public PlayerHistoryAdapter(Context context, RealmList<RealmInt> playerHistory) {
            this.context = context;
            this.history = playerHistory;
            this.metadata = new ArrayList<>();
        }

        @Override
        public int getItemCount() {
            return history.size();
        }

        @Override
        public void onBindViewHolder(PlayerHistoryViewHolder contactViewHolder, int i) {
            contactViewHolder.bind(history.get(i));
        }

        @Override
        public PlayerHistoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scorecard_player_history_item, viewGroup, false);

            return new PlayerHistoryViewHolder(itemView);
        }
    }
}
