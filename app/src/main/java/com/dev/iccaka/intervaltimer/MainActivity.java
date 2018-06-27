package com.dev.iccaka.intervaltimer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity {

    // Current parameters
    private int sets;
    private int workSecs;
    private int workMins;
    private int restSecs;
    private int restMins;
    //========================================================

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

    private static final int DEFAULT_SETS = 12;
    private static final int DEFAULT_WORK_SECS = 30;
    private static final int DEFAULT_WORK_MINS = 1;
    private static final int DEFAULT_REST_SECS = 30;
    private static final int DEFAULT_REST_MINS = 0;
    private static final String DEFAULT_FILE_NAME = "parameters";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {  // The things here should happen only once in the activity's entire lifespan
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        //========================================================

        //attach listeners to all buttons
        this.setsPlusBtn.setOnTouchListener(new RepeatListener(600, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setsPlusBtn.performClick();
            }
        }));
        this.setsMinusBtn.setOnTouchListener(new RepeatListener(600, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setsMinusBtn.performClick();
            }
        }));
        this.workPlusBtn.setOnTouchListener(new RepeatListener(600, 25, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workPlusBtn.performClick();
            }
        }));
        this.workMinusBtn.setOnTouchListener(new RepeatListener(600, 25, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workMinusBtn.performClick();
            }
        }));
        this.restPlusBtn.setOnTouchListener(new RepeatListener(600, 25, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restPlusBtn.performClick();
            }
        }));
        this.restMinusBtn.setOnTouchListener(new RepeatListener(600, 25, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restMinusBtn.performClick();
            }
        }));
        //========================================================

    }

    @Override
    protected void onStart() {

        super.onStart();

        //set the parameters by reading their values from the 'parameters' file
        this.requestReadStoragePermission();

        //get the information on the screen via the custom methods
        this.updateSets();
        this.updateWork();
        this.updateRest();
        //========================================================
    }

    // Methods to read or write the parameters to the corresponding file
    private void setParameters() throws FileNotFoundException {

        // get the parameters from the external storage
        if (this.isExternalStorageReadable()) {
            ArrayList<Integer> parameters = this.readParameters();

            // set the new values
            this.sets = parameters.get(0);
            this.workSecs = parameters.get(1);
            this.workMins = parameters.get(2);
            this.restSecs = parameters.get(3);
            this.restMins = parameters.get(4);
        } else {
            Toast.makeText(this.getApplicationContext(), "Your external storage is currently unavailable.", Toast.LENGTH_LONG).show();
            this.initializeDefaultParameters();
            Toast.makeText(this.getApplicationContext(), "Initialized the default values.", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<Integer> getParameters() {
        ArrayList<Integer> parameters = new ArrayList<>();

        parameters.add(this.sets);
        parameters.add(this.workSecs);
        parameters.add(this.workMins);
        parameters.add(this.restSecs);
        parameters.add(this.restMins);

        return parameters;
    }  // Returns a list containing the current values of the parameters

    // Reads the parameters from the 'parameters' file inside the external storage
    private ArrayList<Integer> readParameters() throws FileNotFoundException {

        ArrayList<Integer> parameters = new ArrayList<>();

        // create a new directory inside the external storage
        File root = new File(Environment.getExternalStorageDirectory(), "Notes");

        // if it doesn't exist...
        if (!root.exists()) {
            // ...create it and all the corresponding files inside needed to function properly
            root.mkdirs();
        }

        // get'parameters' file, from where we will read the values of the parameters
        File gpxfile = new File(root, MainActivity.DEFAULT_FILE_NAME);

        if(gpxfile.length() == 0){
            this.initializeDefaultParameters();
            parameters = this.getParameters();

            return parameters;
        }

        FileReader reader = new FileReader(gpxfile);

        while(reader)

        return parameters;
    }

    // Writes the current parameters from the 'getparameters' method to the parameters file
    private void writeParameters() {
        ArrayList<Integer> parameters = this.getParameters();

        StringBuilder result = new StringBuilder();

        // create a string using the template('sets' 'workSecs' 'workMin' 'restSecs' 'restMins'(
        for (int a : parameters) {
            result.append(a).append(" ");
        }

        // check if the external storage is accessible, using the custom 'isExternalStorageWritable' method, so we can write to the parameters file
        if (this.isExternalStorageWritable()) {
            try {
                // create a new directory inside the external storage
                File root = new File(Environment.getExternalStorageDirectory(), "Notes");

                // if it doesn't exist...
                if (!root.exists()) {
                    // ...create it and all the corresponding files inside needed to function properly
                    root.mkdirs();
                }

                // now create the real 'parameters' file, where we will write the values of the parameters
                File gpxfile = new File(root, MainActivity.DEFAULT_FILE_NAME);
                // create a 'FileWriter' by passing 'gpxfile' to it's constructor
                FileWriter writer = new FileWriter(gpxfile);

                // append the string('StringBuilder result = new StringBuilder()')
                writer.append(result.toString());
                // close the data stream
                writer.close();

            } catch (IOException e) {
                Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {  // if it's not accessible, show a 'Toast'
            Toast.makeText(this.getApplicationContext(), "Your external storage is currently unavailable, the app won't be able to save your values.", Toast.LENGTH_LONG).show();
        }

    }
    //========================================================

    private void initializeDefaultParameters() {
        this.sets = DEFAULT_SETS;
        this.workSecs = DEFAULT_WORK_SECS;
        this.workMins = DEFAULT_WORK_MINS;
        this.restSecs = DEFAULT_REST_SECS;
        this.restMins = DEFAULT_REST_MINS;
    }

    // Methods to check if the external storage is either writable and readable or read-only
    // Checks if the external storage is available for read and write
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        return false;
    }

    // Checks if the external storage is available to at least read
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }

        return false;
    }

    // Custom methods to get the parameters on the screen
    @SuppressLint("SetTextI18n")
    private void updateSets() {
        if (this.sets > 9) {
            this.setsTextView.setText("  " + this.sets + "   ");
            return;
        }

        this.setsTextView.setText("  0" + this.sets + "   ");
    }

    @SuppressLint("SetTextI18n")
    private void updateWork() {
        if (this.workMins > 9 && this.workSecs > 9) {
            this.workTextView.setText("" + this.workMins + " : " + this.workSecs);
        } else if (this.workMins > 9 && this.workSecs <= 9) {
            this.workTextView.setText("" + this.workMins + " : 0" + this.workSecs);
        } else if (this.workMins <= 9 && this.workSecs > 9) {
            this.workTextView.setText("0" + this.workMins + " : " + this.workSecs);
        } else {
            this.workTextView.setText("0" + this.workMins + " : 0" + this.workSecs);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateRest() {
        if (this.restMins > 9 && this.restSecs > 9) {
            this.restTextView.setText("" + this.restMins + " : " + this.restSecs);
        } else if (this.restMins > 9 && this.restSecs <= 9) {
            this.restTextView.setText("" + this.restMins + " : 0" + this.restSecs);
        } else if (this.restMins <= 9 && this.restSecs > 9) {
            this.restTextView.setText("0" + this.restMins + " : " + this.restSecs);
        } else {
            this.restTextView.setText("0" + this.restMins + " : 0" + this.restSecs);
        }
    }
    //========================================================

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

    // Method just to request permission for reading inside the external storage
    // After we receive a result from this method, we go to 'onRequestPermissionResult'
    private void requestReadStoragePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
    }

    // Method just to request permission for writing inside the external storage (it also receives reading permission)
    // After we receive a result from this method, we go to 'onRequestPermissionResult'
    public void requestWriteStoragePermission(View view) {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    /* Method that gets invoked and handles permission results differently
       ('requestCode' 1 is when we enable both write and read a.k.a when we start the
       timer, 2 is only at the begging of the application, where we only
       want to read the values of the parameters from the 'parameters file',
       at 'requestReadStoragePermission' method)
    */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                // if the write permission was granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.writeParameters();
                    this.timerStart();
                } else { // if the permission wasn't granted a.k.a we can't write
                    Toast.makeText(MainActivity.this, "The app won't be able to save your values", Toast.LENGTH_SHORT).show();
                    this.timerStart();
                }
                break;

            case 2:
                // if the 'read only' permission was granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        this.setParameters();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else { // if the permission wasn't granted a.k.a we can't read
                    Toast.makeText(MainActivity.this, "The app won't be able to read your previous values", Toast.LENGTH_SHORT).show();
                    this.initializeDefaultParameters();
                    Toast.makeText(this.getApplicationContext(), "Initialized the default values.", Toast.LENGTH_LONG).show();
                }
        }
    }

    // Method to start the timer and pass the parameters to the TimerActivity class
    private void timerStart() {

        // create an Intent so we can start new activity
        Intent intent = new Intent(this, TimerActivity.class);

        // put the parameters' values inside the intent
        intent.putExtra("sets", this.sets);
        intent.putExtra("workSecs", this.workSecs);
        intent.putExtra("workMins", this.workMins);
        intent.putExtra("restSecs", this.restSecs);
        intent.putExtra("restMins", this.restMins);

        // finally start the activity
        startActivity(intent);

    }
}