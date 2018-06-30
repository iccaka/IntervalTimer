package com.dev.iccaka.intervaltimer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class TimerActivity extends Activity {

    // Views from activity_timer.xml
    private TextView trainingSetsQuantity;
    private TextView trainingWorkQuantity;
    private TextView trainingRestQuantity;
    private TextView trainingMotivationalText;
    private ConstraintLayout thisActivity;
    private Button pauseBtn;
    private Button continueBtn;
    private Button endBtn;
    //========================================================

    // Starting parameters that we get from the main activity
    private int startingSets;
    private int startingWorkSecs;
    private int startingWorkMins;
    private int startingRestSecs;
    private int startingRestMins;
    //========================================================

    // Current parameters
    private int sets;
    private int workSecs;
    private int workMins;
    private int restSecs;
    private int restMins;
    //========================================================

    // Parameters when we stop, so we know where to continue from
    private int pausedSets;
    private int pausedWorkSecs;
    private int pausedWorkMins;
    private int pausedRestSecs;
    private int pausedRestMins;
    //========================================================

    // The two main timers
    private CountDownTimer workCountDownTimer;
    private CountDownTimer restCountDownTimer;
    //========================================================

    // Sounds when we press the buttons
    private MediaPlayer mpToWork;
    private MediaPlayer mpToRest;
    private MediaPlayer mpToPause;
    private MediaPlayer mpToResume;
    private MediaPlayer mpToEnd;
    private MediaPlayer mpToFullyEnd;
    //========================================================

    // Booleans to see if the timer is working and if the timer has been paused
    private boolean isWorkOn;
    private boolean hasBeenPaused;
    //========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {  // The things here should happen only once in the activity's entire lifespan
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        this.isWorkOn = true;
        this.hasBeenPaused = false;

        this.continueBtn = findViewById(R.id.continueBtn);
        this.pauseBtn = findViewById(R.id.pauseBtn);
        this.endBtn = findViewById(R.id.endBtn);
        this.thisActivity = findViewById(R.id.timerActivity);
        this.trainingSetsQuantity = findViewById(R.id.trainingSetsQuantity);
        this.trainingWorkQuantity = findViewById(R.id.trainingWorkQuantity);
        this.trainingRestQuantity = findViewById(R.id.trainingRestQuantity);
        this.trainingMotivationalText = findViewById(R.id.trainingMotivationalText);

        this.endBtn.setVisibility(View.GONE);
        this.continueBtn.setVisibility(View.GONE);
        this.trainingRestQuantity.setVisibility(View.GONE);

        this.thisActivity.setBackgroundColor(Color.RED);

        Bundle mainActivityBundle = getIntent().getExtras();

//        if(savedInstanceState == null){
        this.sets = mainActivityBundle.getInt("sets");
        this.workSecs = mainActivityBundle.getInt("workSecs");
        this.workMins = mainActivityBundle.getInt("workMins");
        this.restSecs = mainActivityBundle.getInt("restSecs");
        this.restMins = mainActivityBundle.getInt("restMins");
        /*}
        else{

        }*/

        this.startingSets = this.sets;
        this.startingWorkSecs = this.workSecs;
        this.startingWorkMins = this.workMins;
        this.startingRestSecs = this.restSecs;
        this.startingRestMins = this.restMins;

        this.mpToWork = MediaPlayer.create(this.getApplicationContext(), R.raw.work);
        this.mpToRest = MediaPlayer.create(this.getApplicationContext(), R.raw.rest);
        this.mpToPause = MediaPlayer.create(this.getApplicationContext(), R.raw.pause);
        this.mpToResume = MediaPlayer.create(this.getApplicationContext(), R.raw.resume);
        this.mpToEnd = MediaPlayer.create(this.getApplicationContext(), R.raw.end);
        this.mpToFullyEnd = MediaPlayer.create(this.getApplicationContext(), R.raw.fullyend);

        this.endBtn.setOnLongClickListener(v -> {
            endTimer(new View(this.getApplicationContext()));
            return false;
        });

        this.createNotificationChannel();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {


    }

    @Override
    protected void onStart() {
        super.onStart();

        this.updateSets();
        this.updateWork();
        this.updateRest();

        this.workCountDownTimer = new CountDownTimer((((this.workMins * 60) + this.workSecs) * 1000) + 2, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                decrementWork(trainingWorkQuantity);
                updateWork();
            }

            @Override
            public void onFinish() {
                decrementSets(trainingSetsQuantity);
                updateSets();
                startRestTimer();
            }
        };

        this.restCountDownTimer = new CountDownTimer((((this.restMins * 60) + this.restSecs) * 1000) + 2, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                decrementRest(trainingRestQuantity);
                updateRest();
            }

            @Override
            public void onFinish() {
                if (sets > 0) {
                    startWorkTimer();
                } else {
                    endTimer(endBtn);
                }
            }
        };


        this.startWorkTimer();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("oneAndOnly", "Timer", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Timer");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Methods to starts the 2 timers
    private void startWorkTimer() {
        this.thisActivity.setBackgroundColor(Color.RED);

        this.mpToWork.start();

        this.workSecs = this.startingWorkSecs;
        this.workMins = this.startingWorkMins;

        this.trainingRestQuantity.setVisibility(View.GONE);
        this.trainingWorkQuantity.setVisibility(View.VISIBLE);

        this.trainingMotivationalText.setText("Work it");

        this.workCountDownTimer.start();
    }

    private void startRestTimer() {

        if (this.startingRestSecs > 0) {
            this.thisActivity.setBackgroundColor(Color.GREEN);

            this.mpToRest.start();

            this.restSecs = this.startingRestSecs;
            this.restMins = this.startingRestMins;

            this.trainingRestQuantity.setVisibility(View.VISIBLE);
            this.trainingWorkQuantity.setVisibility(View.GONE);

            this.trainingMotivationalText.setText("Rest now");

            this.restCountDownTimer.start();
        } else {

        }

    }
    //========================================================

    // Methods to properly increment the parameters when you click on their corresponding buttons
    private void decrementWork(View view) {
        if (this.workMins == 0 && this.workSecs == 1) {
            return;
        }

        if (this.workSecs >= 0) {
            this.workSecs--;

            if (this.workSecs == -1) {
                this.workSecs = 59;
                this.workMins--;

                if (this.workMins == -1) {
                    this.workSecs = 0;
                    this.workMins = 0;
                }
            }
        }

        this.updateWork();
    }

    private void decrementRest(View view) {
        if (this.restSecs >= 0) {
            this.restSecs--;

            if (this.restSecs == -1) {
                this.restSecs = 59;
                this.restMins--;

                if (this.restMins == -1) {
                    this.restSecs = 0;
                    this.restMins = 0;
                }
            }
        }

        this.updateRest();
    }

    private void decrementSets(View view) {
        if (this.sets > 1) {
            this.sets--;
        }

        this.updateSets();
    }
    //========================================================

    // Custom methods to get the parameters on the screen
    @SuppressLint("SetTextI18n")
    private void updateSets() {
        if (this.sets > 9) {
            this.trainingSetsQuantity.setText("" + this.sets);
            return;
        }

        this.trainingSetsQuantity.setText("0" + this.sets);
    }

    @SuppressLint("SetTextI18n")
    private void updateWork() {
        if (this.workMins > 9 && this.workSecs > 9) {
            this.trainingWorkQuantity.setText("" + this.workMins + " : " + this.workSecs);
        } else if (this.workMins > 9 && this.workSecs <= 9) {
            this.trainingWorkQuantity.setText("" + this.workMins + " : 0" + this.workSecs);
        } else if (this.workMins <= 9 && this.workSecs > 9) {
            this.trainingWorkQuantity.setText("0" + this.workMins + " : " + this.workSecs);
        } else {
            this.trainingWorkQuantity.setText("0" + this.workMins + " : 0" + this.workSecs);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateRest() {
        if (this.restMins > 9 && this.restSecs > 9) {
            this.trainingRestQuantity.setText("" + this.restMins + " : " + this.restSecs);
        } else if (this.restMins > 9 && this.restSecs <= 9) {
            this.trainingRestQuantity.setText("" + this.restMins + " : 0" + this.restSecs);
        } else if (this.restMins <= 9 && this.restSecs > 9) {
            this.trainingRestQuantity.setText("0" + this.restMins + " : " + this.restSecs);
        } else {
            this.trainingRestQuantity.setText("0" + this.restMins + " : 0" + this.restSecs);
        }
    }
    //========================================================

    private void updatePausedFields() {
        this.pausedSets = this.sets;
        this.pausedWorkSecs = this.workSecs;
        this.pausedWorkMins = this.workMins;
        this.pausedRestSecs = this.restSecs;
        this.pausedRestMins = this.restMins;
    }

    // Methods to continue, pause and end the timers
    public void continueTimer(View view) {
        this.mpToResume.start();

        if (this.isWorkOn) {
            this.workCountDownTimer = new CountDownTimer((((this.pausedWorkMins * 60) + this.pausedWorkSecs) * 1000) + 1, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    decrementWork(trainingWorkQuantity);
                    updateWork();
                }

                @Override
                public void onFinish() {
                    decrementSets(trainingSetsQuantity);
                    updateSets();
                    startRestTimer();
                }
            };

            this.workCountDownTimer.start();

            this.thisActivity.setBackgroundColor(Color.RED);
        } else {
            this.restCountDownTimer = new CountDownTimer((((this.pausedRestMins * 60) + this.pausedRestSecs) * 1000) + 1, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    decrementRest(trainingRestQuantity);
                    updateRest();
                }

                @Override
                public void onFinish() {
                    if (sets > 0) {
                        startWorkTimer();
                    } else {
                        endTimer(endBtn);
                    }
                }
            };

            this.restCountDownTimer.start();

            this.thisActivity.setBackgroundColor(Color.GREEN);
        }

        this.pauseBtn.setVisibility(View.VISIBLE);
        this.continueBtn.setVisibility(View.GONE);
        this.endBtn.setVisibility(View.GONE);
    }

    public void pauseTimer(View view) {
        this.mpToPause.start();

        if (this.isWorkOn) {
            this.workCountDownTimer.cancel();
        } else {
            this.restCountDownTimer.cancel();
        }


        this.pauseBtn.setVisibility(View.GONE);
        this.continueBtn.setVisibility(View.VISIBLE);
        this.endBtn.setVisibility(View.VISIBLE);

        this.thisActivity.setBackgroundColor(Color.YELLOW);

        this.hasBeenPaused = true;
        this.updatePausedFields();
    }

    public void endTimer(View view) {
        this.mpToEnd.start();

        this.onBackPressed();

    }
    //========================================================

    @Override
    public void onBackPressed() {

        this.workCountDownTimer.cancel();
        this.restCountDownTimer.cancel();

        setResult(MainActivity.RESULT_OK);
        this.finish();

//
//        Intent intent = new Intent(this, TimerActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "oneAndOnly")
//                .setSmallIcon(R.drawable.ic_stat_paused_app)
//                .setContentTitle("Sample title")
//                .setContentText("Sample text")
//                .setAutoCancel(true)
//                .setPriority(NotificationManager.IMPORTANCE_HIGH)
//                .setContentIntent(pendingIntent);  // Open this activity when the notification is pressed

//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.notify("oneAndOnly", 1, mBuilder.build());


    }

}
