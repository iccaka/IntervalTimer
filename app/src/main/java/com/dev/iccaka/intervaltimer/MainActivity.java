package com.dev.iccaka.intervaltimer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class MainActivity extends Activity {

    // Current parameters
    private int sets;
    private int workMins;
    private int workSecs;
    private int restMins;
    private int restSecs;
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

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

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

        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.getAssets().open("parameters")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.getAssets().openFd()));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        
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

        //initialize the default values
        this.sets = 12;
        this.workSecs = 30;
        this.workMins = 1;
        this.restSecs = 30;
        this.restMins = 0;
        //========================================================

        this.setParameters();

        //get the information on the screen via the custom methods
        this.updateSets();
        this.updateWork();
        this.updateRest();
        //========================================================
    }

    // Methods to read/write the parameters to the corresponding file
    private void setParameters() {
        ArrayList<Integer> parameters = this.readParameters();

        this.sets = parameters.get(0);
        this.workSecs = parameters.get(1);
        this.workMins = parameters.get(2);
        this.restSecs = parameters.get(3);
        this.restMins = parameters.get(4);
    }

    private ArrayList<Integer> readParameters() {

        ArrayList<Integer> parameters = new ArrayList<>();

        try {

            //TODO Read the parameters from the 'parameters' file inside the 'res' folder

        } catch (Exception e) {
            e.printStackTrace();
        }

        return parameters;
    }

    private ArrayList<Integer> getParameters() {
        ArrayList<Integer> parameters = new ArrayList<>();

        parameters.add(this.sets);
        parameters.add(this.workSecs);
        parameters.add(this.workMins);
        parameters.add(this.restSecs);
        parameters.add(this.restMins);

        return parameters;
    }

    private void writeParameters() {
        ArrayList<Integer> parameters = this.getParameters();

        try {
            StringBuilder result = new StringBuilder();

            for (int a : parameters) {
                result.append(a).append(" ");
            }

            //TODO Write the parameters to the 'parameters' file inside the 'res' folder

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //========================================================

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

    // Method to start the timer and pass the parameters to the TimerActivity class
    public void timerStart(View view) {

        this.writeParameters();

        Intent intent = new Intent(this, TimerActivity.class);

        intent.putExtra("sets", this.sets);
        intent.putExtra("workSecs", this.workSecs);
        intent.putExtra("workMins", this.workMins);
        intent.putExtra("restSecs", this.restSecs);
        intent.putExtra("restMins", this.restMins);

        startActivity(intent);
    }
}