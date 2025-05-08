package com.md.monitoringsystem.monitorexecution;

import com.md.monitoringsystem.model.Monitor;

import java.net.HttpURLConnection;
import java.net.URL;

public class Task implements Runnable {
    private Monitor monitor;

    Task (Monitor monitor) {
        this.monitor=monitor;
    }
    @Override
    public void run() {
        try{
            String urlString=monitor.getUrl();
            URL url=new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int statusCode = connection.getResponseCode();
            System.out.println(" - Status Code: " + statusCode);
            connection.disconnect();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
