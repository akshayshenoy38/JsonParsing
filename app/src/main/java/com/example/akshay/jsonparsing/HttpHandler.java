package com.example.akshay.jsonparsing;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Akshay on 19-02-2018.
 */

public class HttpHandler {

    private static final String TAG = "HttpHandler";
    public HttpHandler(){

    }

    public String makeServiceCall(String requiredUrl){
        String response = "";

        try {
            URL url = new URL(requiredUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // now reading the response
            InputStream input = new BufferedInputStream(connection.getInputStream());
            response = convertStreamToString(input);



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response;
    }

    private String convertStreamToString(InputStream input) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine())!=null){
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        return stringBuilder.toString();

    }

}
