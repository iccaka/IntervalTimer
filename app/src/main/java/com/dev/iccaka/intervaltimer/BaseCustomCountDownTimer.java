package com.dev.iccaka.intervaltimer;

import android.os.CountDownTimer;

public abstract class BaseCustomCountDownTimer extends CountDownTimer {

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    protected BaseCustomCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    protected void pause(){

    }
}
