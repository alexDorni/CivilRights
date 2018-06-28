package com.example.heroalex.copscivilslicense;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Hero Alex on 6/28/2018.
 */

public class GetDirectionsData extends AsyncTask<Object, String, String> {

    private GoogleMap mMap;
    String urlJSON;
    LatLng origin, dest;

    public GetDirectionsData(CopMap copMap) {
    }

    @Override
    protected String doInBackground(Object... objects) {
        String data = "";
        InputStream iStream = null;


        mMap = (GoogleMap)objects[0];
        urlJSON = (String)objects[1];
        origin = (LatLng)objects[2];
        dest = (LatLng)objects[3];

        HttpURLConnection urlConnection = null;
        try {

            URL url = new URL(urlJSON);

            // construim conexiunea catre Google Api Directions
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            // luam JSON-ul returnat
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            // parsam JSON-ul
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            // il atribuim ca String
            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            try {
                iStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0)
                    .getJSONArray("legs").getJSONObject(0)
                    .getJSONArray("steps");

            int countVariants = jsonArray.length();
            String[] polyline_array = new String[countVariants];

            JSONObject jsonObjectPoints;

            for (int i = 0; i < countVariants; ++i){
                jsonObjectPoints = jsonArray.getJSONObject(i);

                // luam punctele
                String polygone = jsonObjectPoints.getJSONObject("polyline").getString("points");

                polyline_array[i] = polygone;
            }

            int counterPointsArray = polyline_array.length;
            for (int i = 0; i < counterPointsArray; ++i){
                PolylineOptions options = new PolylineOptions();
                options.color(Color.YELLOW);
                options.width(10);
                options.addAll(PolyUtil.decode(polyline_array[i]));

                mMap.addPolyline(options);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}