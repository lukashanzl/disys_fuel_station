package com.example.data_collection_dispatcher.DTOs;

public class Station {
    String id;
    String db_url;
    String lat;
    String lng;

    public Station(){

    }

    public Station(String id, String db_url, String lat, String lng){
        this.id = id;
        this.db_url = db_url;
        this.lat = lat;
        this.lng = lng;
    }
}
