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

import com.dev.iccaka.intervaltimer.Exceptions.DirectoryNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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

    private DataWriter dataWriter;

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
                List<Integer> parameters = this.readParameters();

                // set the new values
                this.sets = parameters.get(0);
                this.workSecs = parameters.get(1);
                this.workMins = parameters.get(2);
                this.restSecs = parameters.get(3);
                this.restMins = parameters.get(4);
            } catch (IOException e) {
                this.initializeDefaultParameters();
            }

        } else {
            Toast.makeText(this.getApplicationContext(), "Your external storage is currently unavailable.", Toast.LENGTH_LONG).show();
            this.initializeDefaultParameters();
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

    private List<Integer> readParameters() throws IOException {  // Reads the parameters from the 'parameters' file inside the external storage

        List<Integer> parameters = new ArrayList<>();

        // create a new directory inside the external storage
        File root = new File(Environment.getExternalStorageDirectory(), "Notes");

        // if it doesn't exist...
        if (!root.exists()) {
            throw new DirectoryNotFoundException("The 'Notes' directory wasn't found");
        }

        // get the 'parameters' file, from where we will read the values of the parameters
        File gpxfile = new File(root, MainActivity.DEFAULT_FILE_NAME);
        // FileReader reader = new FileReader(gpxfile);
        FileInputStream fis = new FileInputStream(gpxfile);

        StringBuilder builder = new StringBuilder();

        while (true) {
            int currChar = fis.read();

            if (currChar == -1) {
                break;
            }

            builder.append((char) currChar);
        }

        String[] values = builder.toString().split(" ");

        for (String value : values) {
            parameters.add(Integer.parseInt(value));
        }

        fis.close();

        return Collections.unmodifiableList(parameters);
    }

    private void writeParameters() {  // Writes the current parameters from the 'getparameters' method to the parameters file
        // get the current values of the parameters and put them into an 'Integer' list
        List<Integer> parameters = this.getParameters();

        // create a StringBuilder where we'll put our data about the parameters
        StringBuilder result = new StringBuilder();

        // create a string using the template('sets' 'workSecs' 'workMin' 'restSecs' 'restMins')
        for (int a : parameters) {
            result.append(a).append(" ");
        }

        /* check if the external storage is accessible, using the custom
           'isExternalStorageWritable' method, so we can write to the parameters file */
        if (this.isExternalStorageWritable()) {
            try {
                // create a new directory inside the external storage
                File root = new File(Environment.getExternalStorageDirectory(), "Notes");

                // if it doesn't exist...
                if (!root.exists()) {
                    // ...create it and all the corresponding files inside needed to function properly
                    root.mkdirs();
                }

                // now create the real 'parameters' file, using the default name, where we will write the values of the parameters
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
            Toast.makeText(this.getApplicationContext(), "Your external storage is currently unavailable, the app won't be able to save your custom values.", Toast.LENGTH_LONG).show();
        }

    }
    //========================================================

    // Gets the current parameters values back to their default ones
    private void initializeDefaultParameters() {
        this.sets = DEFAULT_SETS;
        this.workSecs = DEFAULT_WORK_SECS;
        this.workMins = DEFAULT_WORK_MINS;
        this.restSecs = DEFAULT_REST_SECS;
        this.restMins = DEFAULT_REST_MINS;
    }

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {  // The things here should happen only once in the activity's entire lifespan
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.isBackPressedTwice = false;

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
        this.setsPlusBtn.setOnTouchListener(new RepeatListener(600, 50, v -> setsPlusBtn.performClick()));
        this.setsMinusBtn.setOnTouchListener(new RepeatListener(600, 50, v -> setsMinusBtn.performClick()));
        this.workPlusBtn.setOnTouchListener(new RepeatListener(600, 25, v -> workPlusBtn.performClick()));
        this.workMinusBtn.setOnTouchListener(new RepeatListener(600, 25, v -> workMinusBtn.performClick()));
        this.restPlusBtn.setOnTouchListener(new RepeatListener(600, 25, v -> restPlusBtn.performClick()));
        this.restMinusBtn.setOnTouchListener(new RepeatListener(600, 25, v -> restMinusBtn.performClick()));
        //========================================================


        if (!this.isExternalStorageAccessPermissionGranted()) {
            this.requestWriteStoragePermission();
        }

        if (this.isExternalStorageAccessPermissionGranted()) {
            //set the parameters by reading their values from the 'parameters' file
            this.setParameters();
        } else {
            initializeDefaultParameters();
        }


        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
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

        this.updateData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.setParameters();
        this.updateData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        this.setParameters();
        this.updateData();
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
    public void timerStart(View view) {

        this.writeParameters();

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

    /* Method that gets invoked and handles permission results differently ('requestCode'
       1 is when we enable both write and read a.k.a when we start the timer)
    */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                // if the write permission was granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.setParameters();
                    this.updateData();
                } else { // if the permission wasn't granted a.k.a we can't write
                    Toast.makeText(MainActivity.this, "The app won't be able to save your values", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
            new Handler().postDelayed(() -> isBackPressedTwice = false, 2000);
        }
    }
}
