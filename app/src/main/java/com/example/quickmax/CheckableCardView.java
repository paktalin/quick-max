package com.example.quickmax;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class CheckableCardView extends CardView implements Checkable {
    private boolean isChecked = false;

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    public CheckableCardView(@NonNull Context context) {
        super(context);
        setCardBackgroundColor(
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.selector_card_view_colors
                )
        );
    }

    public CheckableCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setCardBackgroundColor(
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.selector_card_view_colors
                )
        );
    }

    public CheckableCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCardBackgroundColor(
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.selector_card_view_colors
                )
        );
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }
    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;

        ConstraintLayout parent = (ConstraintLayout) this.getParent();
        for(int i = 0; i < 3; i++) {
            CheckableCardView child = (CheckableCardView) parent.getChildAt(i);
            if (child.getId() != this.getId()) {
                child.isChecked = false;
                child.onCreateDrawableState(0);
//                ((TextView) child.getChildAt(0)).setTextColor(-1979711488);
            }

        }
    }
    @Override
    public boolean isChecked() {
        return isChecked;
    }
    @Override
    public void toggle() {
        setChecked(true);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        TextView tv = (TextView) getChildAt(0);

        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else {
            if (tv!= null)
                tv.setTextColor(-1979711488);
        }
        return drawableState;
    }
}