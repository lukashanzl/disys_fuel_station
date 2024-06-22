package com.example.station_data_collector.service;

import com.example.station_data_collector.DTOs.StationData;
import com.example.station_data_collector.communication.Producer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class StationDataCollectorService extends BaseService {

    private final static String BROKER_URL = "localhost";
    private final String id;

    public StationDataCollectorService(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
        this.id = UUID.randomUUID().toString();
        System.out.println("StationDataCollectorService (" + this.id + ") started... ");
    }

    @Override
    protected String executeInternal(String input) {
        System.out.println(input);
        String jobId = UUID.randomUUID().toString();
        System.out.println("StationDataCollectorService (" + jobId + ") executing " + input);

        //Format of the Message String customerID,stationId,dbUrl,lat,lng

        String[] parts = input.split(",");
        if (parts.length != 5) {
            System.err.println("Invalid message format");
            System.err.println("Received message: " + input);
            return "error";
        }

        String customerId = parts[0];
        String stationId = parts[1];
        String dbUrl = parts[2];
        double lat = Double.parseDouble(parts[3]);
        double lng = Double.parseDouble(parts[4]);

        StationData stationData = new StationData();
        try (Connection conn = connectToStationDb(dbUrl)) {
            String sql = "SELECT id, kwh, customer_id FROM station_data WHERE customer_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, customerId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                stationData.setId(rs.getString("id"));
                stationData.setKwh(rs.getDouble("kwh"));
                stationData.setCustomerId(rs.getString("customer_id"));
            } else {
                System.out.println("No data found for customer " + customerId);
                return "error";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }

        String output = String.format("%s,%s,%f", stationData.getId(), stationData.getCustomerId(), stationData.getKwh());

        Producer.send(output, "DataCollectionReceiver", BROKER_URL);
        System.out.println("Sent collected data for Customer " + customerId + " at Station " + stationId);

        return "ok";
    }
    //DB
    private Connection connectToStationDb(String dbUrl) throws SQLException {
        String username = "postgres";
        String password = "postgres";
        return DriverManager.getConnection(dbUrl, username, password);
    }
}
