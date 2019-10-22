package com.example.halifaxtransit.utils;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.halifaxtransit.models.VehiclePositionDataModel;
import com.example.halifaxtransit.viewmodels.MapsActivityViewModel;
import com.google.transit.realtime.GtfsRealtime;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import static com.example.halifaxtransit.Constants.URL_VEHICLE_POSITIONS;

//class which is used for vehicle  position in map

public class VehiclePositionWorker extends Worker {


    public static final String MY_KEY_DATA_FROM_WORKER = "MY_KEY_DATA_FROM_WORKER";
    public static ArrayList<VehiclePositionDataModel> dataset = new ArrayList<>();
    public HashMap map  = new HashMap();
    public Runnable mRunnable;
    public Handler mHandler;

    public VehiclePositionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        new Data.Builder()
                .putAll(map)
                .build();
        getVehiclePositions();
        return Result.success();
    }
    public void getVehiclePositions() {
        mRunnable = () -> {
            try {
                URL url = new URL(URL_VEHICLE_POSITIONS);
                GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(url.openStream());
                for (GtfsRealtime.FeedEntity entity : feed.getEntityList()) {
                    if (entity.hasVehicle()) {

                        VehiclePositionDataModel.VehicleBean currentVehicle = new VehiclePositionDataModel.VehicleBean(
                                entity.getVehicle().getVehicle().getId(),
                                entity.getVehicle().getVehicle().getLabel());

                        VehiclePositionDataModel.TripsBean currentTrip = new VehiclePositionDataModel.TripsBean(
                                entity.getVehicle().getTrip().getTripId(),
                                entity.getVehicle().getTrip().getStartDate(),
                                entity.getVehicle().getTrip().getRouteId()
                        );
                        VehiclePositionDataModel.PositionBean currentPosition = new VehiclePositionDataModel.PositionBean(
                                String.valueOf(entity.getVehicle().getPosition().getLatitude()),
                                String.valueOf(entity.getVehicle().getPosition().getLongitude()),
                                String.valueOf(entity.getVehicle().getPosition().getBearing()),
                                String.valueOf(entity.getVehicle().getPosition().getSpeed())
                        );
                        VehiclePositionDataModel currentVehiclePos = new VehiclePositionDataModel(
                                String.valueOf(entity.getVehicle().getTimestamp()),
                                currentTrip,
                                currentPosition,
                                currentVehicle);

                        dataset.add(currentVehiclePos);
                        map.put(MY_KEY_DATA_FROM_WORKER, dataset);
                        Log.e(ContentValues.TAG, "getVehiclePositions: Route ID: " + entity.getVehicle().getTrip().getRouteId());
                        Log.e(ContentValues.TAG, "getVehiclePositions: Latitude ID: " + entity.getVehicle().getPosition().getLatitude());
                        Log.e(ContentValues.TAG, "getVehiclePositions: Longitude ID: " + entity.getVehicle().getPosition().getLongitude());

                    } }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        mHandler.postDelayed(mRunnable, 10);
    }
}
