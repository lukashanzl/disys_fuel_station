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

    @Override
    protected String executeInternal(String input) {
        System.out.println("DataCollectionReceiverService executing");

        double kwhSUM = 0;
        String customerID = "";
        int i = 0;

        Map<String, Map<String, Double>> stationData = new HashMap<>();

        // split input
        String[] records = input.split(";");
        for (String record : records) {
            String[] parts = record.split(",");
            if (parts.length != 4) {
                System.err.println("Invalid message format");
                System.err.println("Received message: " + record);
                continue; // skip
            }

            // stationID, customerID, chargeID, kwh
            String stationId = parts[0];
            customerID = parts[1];
            String chargeId = parts[2];
            double kwh = Double.parseDouble(parts[3]);
            kwhSUM += kwh;

            stationData
                    .computeIfAbsent(stationId, k -> new HashMap<>())
                    .put(chargeId, kwh);

            i++;
        }

        // output string
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Map<String, Double>> stationEntry : stationData.entrySet()) {
            String stationId = stationEntry.getKey();
            result.append(":\n").append("Station ").append(stationId).append(":\n");

            Map<String, Double> charges = stationEntry.getValue();
            for (Map.Entry<String, Double> chargeEntry : charges.entrySet()) {
                String chargeId = chargeEntry.getKey();
                double kwh = chargeEntry.getValue();
                result.append("    Charge ").append(chargeId).append(": ").append(kwh).append(" kw/h\n");
            }
        }

        result.append("|").append(kwhSUM).append("|").append(customerID);
        combinedResult.append(result).append("\n");

        messageCount++;

        if (messageCount == 3) {
            Producer.send(combinedResult.toString(), "PdfGenerator", BROKER_URL);
            combinedResult.setLength(0); // Clear the StringBuilder
            messageCount = 0; // Reset the message count
        }

        return "ok";
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION);
    }
}