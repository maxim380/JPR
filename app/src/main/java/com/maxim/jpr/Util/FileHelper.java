package com.maxim.jpr.Util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class FileHelper {

    private static final String FILENAME = "songs.txt";

    public static void appendSong(Context context, String song) {
        FileOutputStream outputStream;
        song = song + "\n";

        try {
            outputStream = context.openFileOutput(FILENAME, Context.MODE_APPEND);
            outputStream.write(song.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] getSongList(Context context) {
        ArrayList<String> songs = new ArrayList<>();
        try {
            InputStream inputStream = context.openFileInput(FILENAME);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    songs.add(receiveString);
                }

                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        String[] strings = new String[songs.size()];
        for (int i = 0; i < songs.size(); i++) {
            strings[i] = songs.get(i);
        }

        return strings;
    }

    public static void clearList(Context context) {
        OutputStream outputStream;
        try {
            outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outputStream.write("".getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
