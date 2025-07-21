package com.example.codeleader.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class StringURL {
    public static int getLoc(String stringUrl) {
        int lineCount = 0;
        stringUrl = StringURL.getRawFileURL(stringUrl);
        try {
            URL url = new URL(stringUrl);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            for (String line = reader.readLine() ; line != null ; line = reader.readLine()) {
                lineCount++;
            }
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineCount;
    }

    public static String getRawFileURL(String url){
        url = url.replace("https://github.com/", "https://raw.githubusercontent.com/");
        url = url.replace("blob/", "");
        return url;
    }

    public static String getEditURL(String url){
        url = url.replace("https://github.com/", "https://github.dev/");
        return url;
    }

    public static String getExtension(String url) {
        String[] splitString;
        String extension = "";
        splitString = url.split("\\.");
        extension = splitString[splitString.length - 1];
        return extension;
    }

    public static String getFileName(String url){
        String[] strings = url.split("/");
        return strings[strings.length-1];
    }
}
