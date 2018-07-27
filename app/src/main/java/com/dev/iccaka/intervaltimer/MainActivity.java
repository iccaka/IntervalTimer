package com.dev.iccaka.intervaltimer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.iccaka.intervaltimer.Interfaces.IDataReader;
import com.dev.iccaka.intervaltimer.Interfaces.IDataWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends Activity {

    // Current parameters
    private int sets;
    private int workSecs;
    private int workMins;
    private int restSecs;
    private int restMins;
    //========================================================

    private IDataWriter<Integer> dataWriter;
    private IDataReader<Integer> dataReader;

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

    // Methods to read or write the parameters to the corresponding file
    private void setParameters() {
        // get the parameters from the external storage
        if (this.isExternalStorageReadable()) {

            try {
                List<Integer> parameters = this.dataReader.readData();

                // set the new values
                this.sets = parameters.get(0);
                this.workSecs = parameters.get(1);
                this.workMins = parameters.get(2);
                this.restSecs = parameters.get(3);
                this.restMins = parameters.get(4);
            } catch (IOException e) {
                this.initializeDefaultValues();
            }

        } else {
            Toast.makeText(this.getApplicationContext(), "Your external storage is currently unavailable.", Toast.LENGTH_LONG).show();
            this.initializeDefaultValues();
            Toast.makeText(this.getApplicationContext(), "Initialized the default values.", Toast.LENGTH_LONG).show();
        }
    }

    private List<Integer> getParameters() {  // Returns a list containing the current values of the parameters
        ArrayList<Integer> parameters = new ArrayList<>();

        parameters.add(this.sets);
        parameters.add(this.workSecs);
        parameters.add(this.workMins);
        parameters.add(this.restSecs);
        parameters.add(this.restMins);

        return Collections.unmodifiableList(parameters);
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

    // Checks if the external storage is available to at least read
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }

        return false;
    }

    // Checks if the user had given permission for us to access his/her storage
    private boolean isExternalStorageAccessPermissionGranted() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int result = getApplicationContext().checkCallingOrSelfPermission(permission);

        return (result == PackageManager.PERMISSION_GRANTED);
    }

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

        this.dataReader = new MainActivityDataReader();
        this.dataWriter = new MainActivityDataWriter();

        if (!this.isExternalStorageAccessPermissionGranted()) {
            this.requestWriteStoragePermission();

            if (!this.isExternalStorageAccessPermissionGranted()) {
                this.initializeDefaultValues();
            }
        } else {
            this.setParameters();
        }

        this.updateData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if (getActionBar() != null) {
            getActionBar().hide();
        }

        this.setParameters();
        this.updateData();
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.dataWriter.addData(this.getParameters());
        try {
            this.dataWriter.writeData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        this.dataWriter.addData(this.getParameters());
        try {
            this.dataWriter.writeData();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /* Method used to request permission for writing inside the external storage
    (at the same time it also receives reading permission). After we receive a result
    from this method, we go to 'onRequestPermissionsResult'
    */
    public void requestWriteStoragePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    // Method to start the timer and pass the parameters to the TimerActivity class
    public void timerStart(View view) throws IOException {

        this.dataWriter.addData(this.getParameters());
        this.dataWriter.writeData();

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
