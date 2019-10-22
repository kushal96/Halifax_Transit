package com.example.halifaxtransit.viewmodels;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.example.halifaxtransit.models.VehiclePositionDataModel;
import com.google.transit.realtime.GtfsRealtime;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.halifaxtransit.Constants.CONSTANT_MAP_UPDATE_INTERVAL;
import static com.example.halifaxtransit.Constants.URL_VEHICLE_POSITIONS;

public class MapsActivityViewModel extends ViewModel {

    public Handler mHandler;
    public Runnable mRunnable;
    public HashMap map  = new HashMap();
    MutableLiveData<ArrayList<VehiclePositionDataModel>> mVehiclesLiveData;
    public static ArrayList<VehiclePositionDataModel> dataset = new ArrayList<>();
    public static final String MY_KEY_DATA_FROM_WORKER = "MY_KEY_DATA_FROM_WORKER";

    public void init(LifecycleOwner owner) {
        mHandler = new Handler();
        getVehiclePositions();
        Log.d(TAG, "init: final size "+dataset.size());
    }

    public void setVehicles(ArrayList<VehiclePositionDataModel> buses) {
        mVehiclesLiveData.setValue(buses);
        this.mVehiclesLiveData = mVehiclesLiveData;
    }

    public LiveData<ArrayList<VehiclePositionDataModel>> getVehicles() {
        mVehiclesLiveData = new MutableLiveData<>();
        mVehiclesLiveData.setValue(dataset);
        return mVehiclesLiveData;
    }

    public void getVehiclePositions() {
        mRunnable = () -> {
            CustomAsyncTask asyncTask = new CustomAsyncTask();
            Log.e(TAG, "getVehiclePositions: PreExecute" );
            asyncTask.execute();
            mHandler.postDelayed(mRunnable,CONSTANT_MAP_UPDATE_INTERVAL);
        };
        mHandler.postDelayed(mRunnable, 10);
    }

    public class CustomAsyncTask extends AsyncTask<Void,Void,ArrayList<VehiclePositionDataModel>> {

        @Override
        protected void onPostExecute(ArrayList<VehiclePositionDataModel> buses) {
            super.onPostExecute(buses);
            mVehiclesLiveData.setValue(buses);
            Log.d(TAG, "onPostExecute: local buses:"+buses.size());
            Log.d(TAG, "onPostExecute: global buses:"+dataset.size());

        }

        // Arraylist which collects the data of buses across Halifax
        @Override
        protected ArrayList<VehiclePositionDataModel> doInBackground(Void... voids) {
            ArrayList<VehiclePositionDataModel> buses = new ArrayList<>();
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

                        buses.add(currentVehiclePos);
                        Log.d(TAG, "getVehiclePositions: busList: "+dataset.size());
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
            return buses;
        }
    }
}
