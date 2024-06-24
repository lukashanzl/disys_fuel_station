package com.example.data_collection_receiver.service;

import com.example.data_collection_receiver.communication.Producer;
import java.sql.*;
import java.util.Arrays;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import com.example.data_collection_receiver.service.DataCollectionReceiverService2;

public class DataCollectionReceiverService extends BaseService {

    private final static String DB_CONNECTION = "jdbc:postgresql://localhost:30002/postgres?user=postgres&password=postgres";
    private final static String BROKER_URL = "localhost";
    private final String id;
    private StringBuilder combinedResult;
    private int messageCount;

    public DataCollectionReceiverService(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
        this.id = UUID.randomUUID().toString();
        this.combinedResult = new StringBuilder();
        this.messageCount = 0;
        System.out.println("DataCollectionReceiverService (" + this.id + ") started... ");
    }

    String output = "";
    String custId = "";
    double sum = 0;

    @Override
    protected String executeInternal(String input) {
        System.out.println("DataCollectionReceiverService executing");
        System.out.println("Input: " + input);

        if (input.lastIndexOf(",")<6){
            return "ok";
        }

        sum += Double.parseDouble(input.split(",")[1]);
        custId = input.split(",")[2];

        this.messageCount++;
        checkMessage();

        return "ok";
    }

    private void checkMessage() {
        if (messageCount == 3){
            //send
            output = sum + "," + custId;
            Producer.send(output,"PdfGenerator",BROKER_URL);
            this.messageCount = 0;
            this.output = "";
            this.sum = 0;
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION);
    }
}