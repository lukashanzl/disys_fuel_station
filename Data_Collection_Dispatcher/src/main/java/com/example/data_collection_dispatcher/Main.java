package com.example.data_collection_dispatcher;

import com.example.data_collection_dispatcher.service.DataCollectionDispatcherService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    private static final String BROKER_URL = "localhost";

    public static void main(String[] args) throws IOException, TimeoutException {
        DataCollectionDispatcherService dcdService = new DataCollectionDispatcherService("DataCollectionDispatcher", "StationDataCollector", BROKER_URL);
        dcdService.run();
    }
}
