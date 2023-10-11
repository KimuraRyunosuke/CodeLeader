package com.example.codeleader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class StringURL {
    public static int getLoc(String stringUrl) {
        int lineCount = 0;
        stringUrl = stringUrl.replace("https://github.com/", "https://raw.githubusercontent.com/");
        stringUrl = stringUrl.replace("blob/", "");
        try {
            URL url = new URL(stringUrl);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;
            }
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineCount;
    }
    public static String getExtension(String stringUrl) {
        String[] splitString;
        String extension = "";
        splitString = stringUrl.split("\\.");
        extension = splitString[splitString.length - 1];
        return extension;
    }
}
