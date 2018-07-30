package com.dev.iccaka.intervaltimer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    // Current parameters
    private int sets;
    private int workSecs;
    private int workMins;
    private int restSecs;
    private int restMins;
    //========================================================

    private SharedPreferences sharedPreferences;

    // Views from activity_main.xml
    private TextView setsTextView;
    private TextView workTextView;
    private TextView restTextView;
    private Button setsMinusBtn;
    private Button setsPlusBtn;
    private Button workMinusBtn;
    private Button workPlusBtn;
    private Button restMinusBtn;
    private Button restPlusBtn;
    //========================================================

    private boolean isBackPressedTwice;

    public static final int DEFAULT_SETS = 12;
    public static final int DEFAULT_WORK_SECS = 30;
    public static final int DEFAULT_WORK_MINS = 1;
    public static final int DEFAULT_REST_SECS = 30;
    public static final int DEFAULT_REST_MINS = 0;
    public static final String DEFAULT_FILE_NAME = "parameters";

    // Custom methods to get the parameters on the screen
    private void updateData() {
        this.updateSets();
        this.updateWork();
        this.updateRest();
    }

    @SuppressLint("SetTextI18n")
    private void updateSets() {
        if (this.sets > 9) {
            this.setsTextView.setText(" " + this.sets + "  ");
            return;
        }

        this.setsTextView.setText("  0" + this.sets + "   ");
    }

    @SuppressLint("SetTextI18n")
    private void updateWork() {
        if (this.workMins > 9 && this.workSecs > 9) {
            this.workTextView.setText("" + this.workMins + " : " + this.workSecs);
        } else if (this.workMins > 9) {
            this.workTextView.setText("" + this.workMins + " : 0" + this.workSecs);
        } else if (this.workSecs > 9) {
            this.workTextView.setText("0" + this.workMins + " : " + this.workSecs);
        } else {
            this.workTextView.setText("0" + this.workMins + " : 0" + this.workSecs);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateRest() {
        if (this.restMins > 9 && this.restSecs > 9) {
            this.restTextView.setText("" + this.restMins + " : " + this.restSecs);
        } else if (this.restMins > 9) {
            this.restTextView.setText("" + this.restMins + " : 0" + this.restSecs);
        } else if (this.restSecs > 9) {
            this.restTextView.setText("0" + this.restMins + " : " + this.restSecs);
        } else {
            this.restTextView.setText("0" + this.restMins + " : 0" + this.restSecs);
        }
    }
    //========================================================

    // Gets the current parameters values back to their default ones
    private void initializeDefaultValues() {
        this.sets = DEFAULT_SETS;
        this.workSecs = DEFAULT_WORK_SECS;
        this.workMins = DEFAULT_WORK_MINS;
        this.restSecs = DEFAULT_REST_SECS;
        this.restMins = DEFAULT_REST_MINS;
    }

    private void getViews() {
        //get the views from the R class
        this.setsMinusBtn = findViewById(R.id.setsMinusBtn);
        this.setsPlusBtn = findViewById(R.id.setsPlusBtn);
        this.workMinusBtn = findViewById(R.id.workMinusBtn);
        this.workPlusBtn = findViewById(R.id.workPlusBtn);
        this.restMinusBtn = findViewById(R.id.restMinusBtn);
        this.restPlusBtn = findViewById(R.id.restPlusBtn);
        this.setsTextView = findViewById(R.id.setsQuantity);
        this.workTextView = findViewById(R.id.workQuantity);
        this.restTextView = findViewById(R.id.restQuantity);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void attachListenersToButtons() {
        //attach listeners to all buttons
        this.setsPlusBtn.setOnTouchListener(new RepeatListener(600, 50, v -> setsPlusBtn.performClick()));
        this.setsMinusBtn.setOnTouchListener(new RepeatListener(600, 50, v -> setsMinusBtn.performClick()));
        this.workPlusBtn.setOnTouchListener(new RepeatListener(600, 25, v -> workPlusBtn.performClick()));
        this.workMinusBtn.setOnTouchListener(new RepeatListener(600, 25, v -> workMinusBtn.performClick()));
        this.restPlusBtn.setOnTouchListener(new RepeatListener(600, 25, v -> restPlusBtn.performClick()));
        this.restMinusBtn.setOnTouchListener(new RepeatListener(600, 25, v -> restMinusBtn.performClick()));
    }

    private void setTypeFacesToViews() {
        Typeface tf = ResourcesCompat.getFont(getApplicationContext(), R.font.monkey);
        TextView stv = findViewById(R.id.setsText);
        TextView wtv = findViewById(R.id.workText);
        TextView rtv = findViewById(R.id.restText);
        Button startBtn = findViewById(R.id.startBtn);

        stv.setTypeface(tf);
        wtv.setTypeface(tf);
        rtv.setTypeface(tf);
        startBtn.setTypeface(tf);
        this.setsMinusBtn.setTypeface(tf);
        this.setsPlusBtn.setTypeface(tf);
        this.workMinusBtn.setTypeface(tf);
        this.workPlusBtn.setTypeface(tf);
        this.restMinusBtn.setTypeface(tf);
        this.restPlusBtn.setTypeface(tf);
        this.setsTextView.setTypeface(tf);
        this.workTextView.setTypeface(tf);
        this.restTextView.setTypeface(tf);
    }

    private void getSharedPreferences() {
        this.sharedPreferences = getSharedPreferences(DEFAULT_FILE_NAME, Context.MODE_PRIVATE);
        if (this.sharedPreferences.contains("sets")) {
            this.sets = this.sharedPreferences.getInt("sets", DEFAULT_SETS);
            this.workSecs = this.sharedPreferences.getInt("workSecs", DEFAULT_WORK_SECS);
            this.workMins = this.sharedPreferences.getInt("workMins", DEFAULT_WORK_MINS);
            this.restSecs = this.sharedPreferences.getInt("restSecs", DEFAULT_REST_SECS);
            this.restMins = this.sharedPreferences.getInt("restMins", DEFAULT_REST_MINS);
        } else {
            this.initializeDefaultValues();
        }
    }

    @SuppressLint("ApplySharedPref")
    private void putSharedPreferences() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt("sets", this.sets);
        editor.putInt("workSecs", this.workSecs);
        editor.putInt("workMins", this.workMins);
        editor.putInt("restSecs", this.restSecs);
        editor.putInt("restMins", this.restMins);
        editor.commit();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {  // The things here should happen only once in the activity's entire lifespan
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.isBackPressedTwice = false;

        this.getViews();
        this.attachListenersToButtons();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            this.setTypeFacesToViews();
        }

        this.getSharedPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getActionBar() != null) {
            getActionBar().hide();
        }

        this.getSharedPreferences();
        this.updateData();
    }

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onPause() {
        super.onPause();

        this.putSharedPreferences();
    }

    // Methods to properly increment the parameters when you click on their corresponding buttons
    public void incrementSets(View view) {
        this.sets++;

        if (this.sets == 100) {
            this.sets = 1;
        }

        this.updateSets();
    }

    public void decrementSets(View view) {
        if (this.sets > 1) {
            this.sets--;
        }

        this.updateSets();
    }

    public void incrementWork(View view) {
        this.workSecs++;

        if (this.workSecs == 60) {
            this.workSecs = 0;
            this.workMins++;

            if (this.workMins == 60) {
                this.workSecs = 0;
                this.workMins = 0;
            }
        }

        this.updateWork();
    }

    public void decrementWork(View view) {
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

    public void incrementRest(View view) {
        this.restSecs++;

        if (this.restSecs == 60) {
            this.restSecs = 0;
            this.restMins++;

            if (this.restMins == 60) {
                this.restSecs = 0;
                this.restMins = 0;
            }
        }

        this.updateRest();
    }

    public void decrementRest(View view) {
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
    //========================================================

    // Method to start the timer and pass the parameters to the TimerActivity class
    @SuppressLint("ApplySharedPref")
    public void timerStart(View view) {

        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt("sets", this.sets);
        editor.putInt("workSecs", this.workSecs);
        editor.putInt("workMins", this.workMins);
        editor.putInt("restSecs", this.restSecs);
        editor.putInt("restMins", this.restMins);
        editor.commit();

        // create an 'Intent' so we can start the new activity
        Intent intent = new Intent(this, TimerActivity.class);
        // put the parameters' values inside the 'Intent'
        intent.putExtra("sets", this.sets);
        intent.putExtra("workSecs", this.workSecs);
        intent.putExtra("workMins", this.workMins);
        intent.putExtra("restSecs", this.restSecs);
        intent.putExtra("restMins", this.restMins);

        // finally start the activity
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // if the 'isBackPressedTwice' boolean is 'true', invoke the super method which only exits the activity
        if (this.isBackPressedTwice) {
            super.onBackPressed();
        } else {
            //... if it isn't pressed, make the boolean 'true' and show a 'Toast' with a message
            this.isBackPressedTwice = true;
            Toast.makeText(MainActivity.this, "Press BACK once again to exit", Toast.LENGTH_SHORT).show();

            // start a 'Handler' and if we don't press 'BACK' again in 2 seconds, the boolean gets back to 'false' and the whole thing starts again after we press 'BACK'
            new Handler().postDelayed(() -> this.isBackPressedTwice = false, 2000);
        }
    }
}
