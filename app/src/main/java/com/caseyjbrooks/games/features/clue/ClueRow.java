package com.caseyjbrooks.games.features.clue;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.caseyjbrooks.games.R;

public class ClueRow extends HorizontalScrollView {

    TextView rowNameTextView;

    public ClueRow(Context context) {
        super(context);
        init(null, 0);
    }

    public ClueRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ClueRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.clue_row, this);

        rowNameTextView = (TextView) view.findViewById(R.id.row_name);

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.ClueRow,
                defStyle,
                0);

        String name = a.getString(R.styleable.ClueRow_rowName);

        a.recycle();

        setName(name);
    }

    public void setName(String name) {
        if(name != null && name.length() > 0) {
            rowNameTextView.setText(name);
        }
    }
}
