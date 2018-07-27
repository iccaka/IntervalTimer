package com.dev.iccaka.intervaltimer;

import android.os.Environment;

import com.dev.iccaka.intervaltimer.Exceptions.DirectoryNotFoundException;
import com.dev.iccaka.intervaltimer.Interfaces.IDataReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivityDataReader implements IDataReader<Integer> {

    public MainActivityDataReader(){

    }

    @Override
    public List<Integer> readData() throws IOException {
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
}
