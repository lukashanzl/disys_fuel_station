package com.example.data_collection_receiver.service;

import com.example.data_collection_receiver.communication.Producer;
import java.sql.*;
import java.util.Arrays;
import java.util.UUID;

public class DataCollectionReceiverService2 extends BaseService{

    private final static String DB_CONNECTION = "jdbc:postgresql://localhost:30002/postgres?user=postgres&password=postgres";
    private final static String BROKER_URL = "localhost";
    private final String id;
    String startID;

    public DataCollectionReceiverService2 (String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
        this.id = UUID.randomUUID().toString();
        System.out.println("DataCollectionReceiverService2 (" + this.id + ") started... ");
    }

    @Override
    protected String executeInternal(String input) {
        System.out.println("DataCollectionReceiverService2 executing");
        startID = input;
        return input;
    }

    public String startID(){
        return startID;
    }
}
