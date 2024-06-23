package com.example.data_collection_receiver;

import com.example.data_collection_receiver.service.DataCollectionReceiverService;
import com.example.data_collection_receiver.service.DataCollectionReceiverService2;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    private static final String BROKER_URL = "localhost";

    public static void main(String[] args) throws IOException, TimeoutException {
        DataCollectionReceiverService dcr1Service = new DataCollectionReceiverService("DataCollectionReceiver", "PdfGenerator", BROKER_URL);
        //DataCollectionReceiverService2 dcr2Service = new DataCollectionReceiverService2("StationDataCollector", "PdfGenerator", BROKER_URL);
        //dcr2Service.run();
        dcr1Service.run();
    }
}
