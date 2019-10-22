package com.example.halifaxtransit;

public class Constants  {
    public  static final String URL_TRIP_UPDATES ="http://gtfs.halifax.ca/realtime/TripUpdate/TripUpdates.pb";
    public  static final String URL_ALERTS ="http://gtfs.halifax.ca/realtime/Alert/Alerts.pb";
    public  static final String URL_VEHICLE_POSITIONS ="http://gtfs.halifax.ca/realtime/Vehicle/VehiclePositions.pb";
    public  static final int CONSTANT_MAP_UPDATE_INTERVAL = 8000;

    static final String TABLE_AGENCY = "agency";
    static final String TABLE_CALENDAR = "calendar";
    static final String TABLE_CALENDAR_DATES = "calendar_dates";
    static final String TABLE_FEED_INFO = "feed_info";
    static final String TABLE_ROUTES = "routes";
    static final String TABLE_SHAPES = "shapes";

    static final String TABLE_STOP_TIMES = "stop_times";
    static final String ATTR_TRIP_ID = "trip_id";
    static final String ATTR_STOP_SEQUENCE = "stop_sequence";




    static final String TABLE_STOPS = "stops";
    static final String TABLE_TRIPS = "trips";

}
