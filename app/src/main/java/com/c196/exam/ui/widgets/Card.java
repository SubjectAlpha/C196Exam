package com.c196.exam.ui.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class Card extends CardView {

    public Card(@NonNull Context context) {
        super(context);
    }

    public Card(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Card(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static Card createCard(Context c, int termId, int index) {
        int cardColor = Color.argb(
                200, 240, 240, 240
        );

        int height = 150;
        int radius = 10;
        int topMargin = 25;

        Card card = new Card(c);

        card.setMinimumHeight(height);
        card.setRadius(radius);
        card.setCardBackgroundColor(cardColor);
        card.setPadding(10,0,0,0);
        card.setContentDescription("" + termId);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.25f);

        if(index == 0){
            topMargin = 0;
        }
        params.setMargins(0, topMargin, 0, 10);
        card.setLayoutParams(params);

        return card;
    }
}
