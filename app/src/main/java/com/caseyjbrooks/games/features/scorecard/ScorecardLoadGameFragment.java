package com.caseyjbrooks.games.features.scorecard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.caseyjbrooks.games.R;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class ScorecardLoadGameFragment extends Fragment {

    ListView savedGames;

    public static ScorecardLoadGameFragment newInstance() {
        ScorecardLoadGameFragment fragment = new ScorecardLoadGameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scorecard_load_game, container, false);

        savedGames = (ListView) view.findViewById(R.id.savedGamesList);

        Realm realm = Realm.getInstance(getContext());
        RealmResults<ScorecardGame> games = realm.where(ScorecardGame.class).findAll();

        savedGames.setAdapter(new GamesAdapter(getContext(), games, true));

        return view;
    }

//Adapter for the games in the list
    private static class GamesAdapter extends RealmBaseAdapter<ScorecardGame> implements ListAdapter {
        private static class GameViewHolder {
            View root;
            TextView name;
            TextView dateCreated;
            TextView dateModified;
            TextView playerNames;
        }

        public GamesAdapter(Context context, RealmResults<ScorecardGame> realmResults, boolean automaticUpdate) {
            super(context, realmResults, automaticUpdate);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GameViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.scorecard_game_list_item,
                        parent, false);
                viewHolder = new GameViewHolder();
                viewHolder.root = convertView.findViewById(R.id.root);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.dateCreated = (TextView) convertView.findViewById(R.id.dateCreated);
                viewHolder.dateModified = (TextView) convertView.findViewById(R.id.dateModified);
                viewHolder.playerNames = (TextView) convertView.findViewById(R.id.playerNames);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (GameViewHolder) convertView.getTag();
            }

            final ScorecardGame item = realmResults.get(position);
            viewHolder.name.setText(item.getName());
            viewHolder.dateCreated.setText(item.getDateCreated().toString());
            viewHolder.dateModified.setText(item.getDateModified().toString());
            viewHolder.playerNames.setText(item.getPlayers().size() + " players");
            viewHolder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("GAME_ID", item.getKey());
                    Intent intent = new Intent(v.getContext(), ScorecardActivity.class);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });

            return convertView;
        }

        public RealmResults<ScorecardGame> getRealmResults() {
            return realmResults;
        }
    }
}
