package com.caseyjbrooks.games;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

/**
 * TODO: document your custom view class.
 */
public class TricolorCheckbox extends CheckBox {


    public TricolorCheckbox(Context context) {
        super(context);
        init(null, 0);
    }

    public TricolorCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TricolorCheckbox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        state = 0;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                state = (state+1)%4;
                if(state == 0) {
                    setChecked(false);
                    Drawable drawable = getResources().getDrawable(R.drawable.checkbox0);
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    TricolorCheckbox.this.setButtonDrawable(drawable);
                }
                else if(state == 1) {
                    setChecked(true);
                    Drawable drawable = getResources().getDrawable(R.drawable.circle_correct);
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    TricolorCheckbox.this.setButtonDrawable(drawable);
                }
                else if(state == 2) {
                    setChecked(true);
                    Drawable drawable = getResources().getDrawable(R.drawable.circle_unknown);
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    TricolorCheckbox.this.setButtonDrawable(drawable);
                }
                else if(state == 3) {
                    setChecked(true);
                    Drawable drawable = getResources().getDrawable(R.drawable.circle_missing);
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    TricolorCheckbox.this.setButtonDrawable(drawable);
                }
            }
        });

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.TricolorCheckbox,
                defStyle,
                0);

        int color = a.getColor(R.styleable.TricolorCheckbox_drawableTint, Color.BLACK);

        a.recycle();

        setTintColor(color);
    }

    public void setTintColor(int color) {
        this.color = color;

        setChecked(false);
        Drawable drawable = getResources().getDrawable(R.drawable.checkbox0);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        TricolorCheckbox.this.setButtonDrawable(drawable);
    }

    int state;
    int color;
}
