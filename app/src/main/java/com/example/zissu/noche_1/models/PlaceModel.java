package com.example.zissu.noche_1.models;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zissu on 26/05/2017.
 */

public class PlaceModel {
    private String name;
    private List<Location> locations;
    private String openingHours;
    private String web;
    private String phone;
    private String rank;
    private String urlFront;
    private String urlInside;
    private String line;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUrlFront() {
        return urlFront;
    }

    public void setUrlFront(String urlFront) {
        this.urlFront = urlFront;
    }

    public String getUrlInside() {
        return urlInside;
    }

    public void setUrlInside(String urlInside) {
        this.urlInside = urlInside;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }


    public static class Location {
        private double lat;
        private double lon;
        private String city;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

    }
}
