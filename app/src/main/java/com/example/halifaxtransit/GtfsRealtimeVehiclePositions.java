package com.example.halifaxtransit;

import android.util.Log;

import com.google.transit.realtime.GtfsRealtime;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;

import static android.content.ContentValues.TAG;
import static com.example.halifaxtransit.Constants.URL_TRIP_UPDATES;
import static com.example.halifaxtransit.Constants.URL_VEHICLE_POSITIONS;

//[1]
public class GtfsRealtimeVehiclePositions {
    public static void main(String[] args) {
        System.out.println("Main Method entry point");
        try {
            URL url = new URL(URL_VEHICLE_POSITIONS);
            GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(url.openStream());
            for (GtfsRealtime.FeedEntity entity : feed.getEntityList()) {
                if (entity.hasVehicle()) {
//                    System.out.println(entity.getVehicle());
                    System.out.println(entity.getVehicle().getTrip().getRouteId());
                    System.out.println(entity.getVehicle().getPosition().getLatitude());
                    System.out.println(entity.getVehicle().getPosition().getLongitude());
//                    Log.e(TAG, "main: ROUTE ID" + entity.getTripUpdate().getTrip().getRouteId());
//                    Log.e(TAG, "main: TRIP ID" + entity.getTripUpdate().getTrip().getTripId());
//                    Log.e(TAG, "main: VEHICLE ID" + entity.getTripUpdate().getVehicle().getId());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}