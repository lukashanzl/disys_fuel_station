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
            System.out.println("Received message: " + input);
            return "";
        }
        String customerId = parts[0];
        String stationId = parts[1];
        String dbUrl = "jdbc:postgresql://"+parts[2]+"/stationdb?user=postgres&password=postgres";
        System.out.println(dbUrl);

        String output = "";

        StationData stationData = new StationData();
        try (Connection conn = connectToStationDb(dbUrl)) {
            String sql = "SELECT  sum(kwh) as sum, customer_id FROM charge WHERE customer_id = ? group by customer_id";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(customerId));
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                output = stationId + "," + rs.getString("sum") + "," + rs.getString("customer_id");
                Producer.send(output,"DataCollectionReceiver",BROKER_URL);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }


        return "ok";
    }


    //DB
    private Connection connectToStationDb(String dbUrl) throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }
}
