package com.example.halifaxtransit.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {
    private LatLng position;
    private String title;

    public int getIcon() {
        return icon;
    }

    private int icon;

    public ClusterMarker() {
    }

    public ClusterMarker(LatLng position, String title, int icon) {
        this.position = position;
        this.title = title;
        this.icon = icon;
    }

    @Override
    public LatLng getPosition() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
