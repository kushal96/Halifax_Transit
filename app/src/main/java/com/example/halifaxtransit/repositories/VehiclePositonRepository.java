package com.example.halifaxtransit.repositories;


//This maintains the repository for the data of longitude and lattitude for the locations
import android.util.Log;

import com.example.halifaxtransit.Constants;
import com.example.halifaxtransit.models.VehiclePositionDataModel;
import com.google.transit.realtime.GtfsRealtime;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

import static android.content.ContentValues.TAG;
import static com.example.halifaxtransit.Constants.URL_VEHICLE_POSITIONS;

public class VehiclePositonRepository {

    static VehiclePositonRepository mInstance;

    public void setDataset(ArrayList<VehiclePositionDataModel> dataset) {
        this.dataset = dataset;
    }

    ArrayList<VehiclePositionDataModel> dataset = new ArrayList<>();

    public MutableLiveData<ArrayList<VehiclePositionDataModel>> getDataset() {

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
                    Log.e(TAG, "onChanged: Route ID: " + entity.getVehicle().getTrip().getRouteId());
                    Log.e(TAG, "onChanged: Latitude ID: " + entity.getVehicle().getPosition().getLatitude());
                    Log.e(TAG, "onChanged: Longitude ID: " + entity.getVehicle().getPosition().getLongitude());
                    System.out.println(entity.getVehicle().getTrip().getRouteId());
                    System.out.println(entity.getVehicle().getPosition().getLatitude());
                    System.out.println(entity.getVehicle().getPosition().getLongitude());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MutableLiveData<ArrayList<VehiclePositionDataModel>> data = new MutableLiveData<>();
        data.setValue(dataset);
        return data;
    }


    public static VehiclePositonRepository getInstance() {
        if (mInstance == null) {
            mInstance = new VehiclePositonRepository();
        }
        return mInstance;
    }

}
