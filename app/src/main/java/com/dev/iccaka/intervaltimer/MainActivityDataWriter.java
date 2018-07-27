package com.dev.iccaka.intervaltimer;

import android.os.Environment;

import com.dev.iccaka.intervaltimer.Interfaces.IDataWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivityDataWriter implements IDataWriter {

    private List<Integer> dataList;

    public MainActivityDataWriter(List<Integer> data) {
        this.dataList = new ArrayList<>();
        this.dataList.addAll(data);
    }

    @Override
    public void writeData() throws IOException {
        // create a StringBuilder where we'll put our data about the parameters
        StringBuilder result = new StringBuilder();

        // create a string using the template('sets' 'workSecs' 'workMin' 'restSecs' 'restMins')
        for (int data : this.dataList) {
            result.append(data).append(" ");
        }

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
                e.printStackTrace();
            }
        } else {  // if it's not accessible, show a 'Toast'
//            Toast.makeText(this.getApplicationContext(), "Your external storage is currently unavailable, the app won't be able to save your custom values.", Toast.LENGTH_LONG).show();
//            return 1;
            throw new IOException("Your external storage is currently unavailable, the app won't be able to save your custom values.");
        }
    }

    // Checks if the external storage is available for both read and write operations
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        return false;
    }
}
