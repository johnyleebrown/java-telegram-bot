package com.moviebuddy.services;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.telegram.telegrambots.logging.BotLogger;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Greg
 * @version 1.0
 * @brief Tranform address into coordinates via google's api
 * @date 08/14/16
 */

public class ToCoordinates {

    private static final String LOGTAG = "TOCOORDINATESSERVICE";
    private static volatile ToCoordinates instance;

    /**
     * Constructor (private due to singleton pattern)
     */
    private ToCoordinates() {
    }

    /**
     * Singleton
     *
     * @return Return the instance of this class
     */
    public static ToCoordinates getInstance() {
        ToCoordinates currentInstance;
        if (instance == null) {
            synchronized (ToCoordinates.class) {
                if (instance == null) {
                    instance = new ToCoordinates();
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }
        return currentInstance;
    }


    public static double[] getCoord(String address){

        String add = "";
        try {
            add = URLEncoder.encode(address,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        double[] coord = new double[2];

        try {
            String url = "http://maps.google.com/maps/api/geocode/json?address=" + add + "/";

            HttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            HttpEntity ht = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            String responseContent = EntityUtils.toString(buf, "UTF-8");
            JSONObject jsonObject = new JSONObject(responseContent);

            if (jsonObject.getString("status").equals("OK")) {
                JSONObject obj = jsonObject.getJSONArray("results").getJSONObject(0);
                JSONObject obj1 = obj.getJSONObject("geometry");
                JSONObject obj2 = obj1.getJSONObject("location");
                coord[0] = obj2.getDouble("lat");
                coord[1] = obj2.getDouble("lng");
            } else {
                BotLogger.warn(LOGTAG, "Not found");
            }

        } catch (Exception e) {
            BotLogger.warn(LOGTAG, e);
        }

        return coord;
    }

}
