package com.dev.iccaka.intervaltimer;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;


public class RepeatListener implements OnTouchListener {

    private Handler handler = new Handler();
    private View downView;
    private int initialInterval;
    private final int normalInterval;
    private final OnClickListener clickListener;

    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, normalInterval);
            clickListener.onClick(downView);
        }
    };

    public RepeatListener(int initialInterval, int normalInterval, View.OnClickListener clickListener) {
        if (clickListener == null) {
            throw new IllegalArgumentException("null runnable");
        }

        if (initialInterval < 0 || normalInterval < 0) {
            throw new IllegalArgumentException("negative interval");
        }

        this.initialInterval = initialInterval;
        this.normalInterval = normalInterval;
        this.clickListener = clickListener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {

            case MotionEvent.ACTION_DOWN: {
                handler.removeCallbacks(handlerRunnable);
                handler.postDelayed(handlerRunnable, initialInterval);
                downView = view;
                downView.setPressed(true);
                clickListener.onClick(view);
                return true;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                handler.removeCallbacks(handlerRunnable);
                downView.setPressed(false);
                downView = null;
                return true;
            }
        }

        return false;
    }

}
