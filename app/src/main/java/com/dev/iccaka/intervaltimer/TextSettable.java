package com.dev.iccaka.intervaltimer;


import android.content.Context;

public class TextSettable extends android.support.v7.widget.AppCompatTextView {

    public TextSettable(Context context) {
        super(context);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }
}