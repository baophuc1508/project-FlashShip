package com.example.paul.flashship.Dowload;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseLatLng {

    String data;

    public ParseLatLng(String data) {
        this.data = data;
    }


    public Location getLatLng() {
        Location location = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsonObject1 = jsonObject.getJSONObject("Response");
            JSONArray resultArray = jsonObject1.getJSONArray("View");

            for (int i = 0; i < resultArray.length();i++){
                JSONObject objectResult = resultArray.getJSONObject(i);

                JSONArray jsonArray = objectResult.getJSONArray("Result");
                for(int j= 0; j < jsonArray.length(); j++ ){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(j);

                    JSONObject jsonObject3 = jsonObject2.getJSONObject("Location");
                    JSONObject jsonObject4 = jsonObject3.getJSONObject("DisplayPosition");

                    Double lat = jsonObject4.getDouble("Latitude");
                    Double lng = jsonObject4.getDouble("Longitude");

                    location = new Location("");
                    location.setLatitude(lat);
                    location.setLongitude(lng);
                }






            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return location;
    }
}
