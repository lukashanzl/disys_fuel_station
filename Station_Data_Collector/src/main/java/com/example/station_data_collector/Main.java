package com.example.station_data_collector;

import com.example.station_data_collector.service.StationDataCollectorService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    private static final String BROKER_URL = "localhost";

    public static void main(String[] args) throws IOException, TimeoutException {
        StationDataCollectorService dcdService = new StationDataCollectorService("StationDataCollector", "DataCollectionReceiver", BROKER_URL);
        dcdService.run();
    }
}
