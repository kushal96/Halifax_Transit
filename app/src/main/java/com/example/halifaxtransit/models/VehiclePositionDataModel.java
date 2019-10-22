package com.example.halifaxtransit.models;
//getter and setter variables and methods
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class VehiclePositionDataModel {

    //    trip {
//        trip_id: "17476405"
//        start_date: "20190707"
//        route_id: "320"
//    }
//    position {
//        latitude: 44.65072
//        longitude: -63.5797
//        bearing: 90.0
//        speed: 11.176
//    }
//    timestamp: 1562544134
//    vehicle {
//        id: "2527"
//        label: "527"
//    }
    String timestamp;
    TripsBean trip;
    PositionBean position;
    VehicleBean vehicle;
    ArrayList<TripsBean> tripsList = new ArrayList<>();
    ArrayList<TripsBean> positionList = new ArrayList<>();
    ArrayList<TripsBean> vehicleList = new ArrayList<>();

    public VehiclePositionDataModel(String timestamp, TripsBean trip, PositionBean position, VehicleBean vehicle) {
        this.timestamp = timestamp;
        this.trip = trip;
        this.position = position;
        this.vehicle = vehicle;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public TripsBean getTrip() {
        return trip;
    }

    public void setTrip(TripsBean trip) {
        this.trip = trip;
    }

    public PositionBean getPosition() {
        return position;
    }

    public void setPosition(PositionBean position) {
        this.position = position;
    }

    public VehicleBean getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleBean vehicle) {
        this.vehicle = vehicle;
    }

    public ArrayList<TripsBean> getTripsList() {
        return tripsList;
    }

    public void setTripsList(ArrayList<TripsBean> tripsList) {
        this.tripsList = tripsList;
    }

    public ArrayList<TripsBean> getPositionList() {
        return positionList;
    }

    public void setPositionList(ArrayList<TripsBean> positionList) {
        this.positionList = positionList;
    }

    public ArrayList<TripsBean> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(ArrayList<TripsBean> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public static class TripsBean implements Parcelable {
        public String trip_id;
        public String start_date;
        public String route_id;

        public TripsBean(String trip_id, String start_date, String route_id) {
            this.trip_id = trip_id;
            this.start_date = start_date;
            this.route_id = route_id;
        }

        protected TripsBean(Parcel in) {
            trip_id = in.readString();
            start_date = in.readString();
            route_id = in.readString();
        }

        public  final Creator<TripsBean> CREATOR = new Creator<TripsBean>() {
            @Override
            public TripsBean createFromParcel(Parcel in) {
                return new TripsBean(in);
            }

            @Override
            public TripsBean[] newArray(int size) {
                return new TripsBean[size];
            }
        };

        public String getTrip_id() {
            return trip_id;
        }

        public void setTrip_id(String trip_id) {
            this.trip_id = trip_id;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getRoute_id() {
            return route_id;
        }

        public void setRoute_id(String route_id) {
            this.route_id = route_id;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(trip_id);
            dest.writeString(start_date);
            dest.writeString(route_id);
        }
    }

    public static class PositionBean implements Parcelable{
        String latitude;
        String longitude;
        String bearing;
        String speed;

        public PositionBean(String latitude, String longitude, String bearing, String speed) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.bearing = bearing;
            this.speed = speed;
        }

        protected PositionBean(Parcel in) {
            latitude = in.readString();
            longitude = in.readString();
            bearing = in.readString();
            speed = in.readString();
        }

        public  final Creator<PositionBean> CREATOR = new Creator<PositionBean>() {
            @Override
            public PositionBean createFromParcel(Parcel in) {
                return new PositionBean(in);
            }

            @Override
            public PositionBean[] newArray(int size) {
                return new PositionBean[size];
            }
        };

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getBearing() {
            return bearing;
        }

        public void setBearing(String bearing) {
            this.bearing = bearing;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(latitude);
            dest.writeString(longitude);
            dest.writeString(bearing);
            dest.writeString(speed);
        }
    }

    public static class VehicleBean implements Parcelable {
        String id, label;

        public VehicleBean(String id, String label) {
            this.id = id;
            this.label = label;
        }

        protected VehicleBean(Parcel in) {
            id = in.readString();
            label = in.readString();
        }

        public  final Creator<VehicleBean> CREATOR = new Creator<VehicleBean>() {
            @Override
            public VehicleBean createFromParcel(Parcel in) {
                return new VehicleBean(in);
            }

            @Override
            public VehicleBean[] newArray(int size) {
                return new VehicleBean[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(label);
        }
    }
}
