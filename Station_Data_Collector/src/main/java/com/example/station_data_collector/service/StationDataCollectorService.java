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
        String output="";
        String customerId = parts[0];
        String stationId = parts[1];
        String dbUrl = "jdbc:postgresql://"+parts[2]+"/postgres?user=postgres&password=postgres";
        System.out.println(dbUrl);

        StationData stationData = new StationData();
        try (Connection conn = connectToStationDb(dbUrl)) {
            String sql = "SELECT id, kwh, customer_id FROM charge WHERE customer_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(customerId));
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {

                stationData.setId(rs.getString("id"));
                stationData.setKwh(rs.getDouble("kwh"));
                stationData.setCustomerId(rs.getString("customer_id"));
                System.out.println("ResultSet:");
                do {
                    // ChargeID,kwh,CustomerID
                    output += rs.getString("id")+",";
                    output += rs.getDouble("kwh")+",";
                    output += rs.getString("customer_id")+";";
                } while (rs.next());
                System.out.println(output);
                Producer.send(output, "DataCollectionReceiver", BROKER_URL);
                System.out.println("Sent collected data for Customer " + customerId + " at Station " + stationId);
            } else {
                System.out.println("No data found for customer " + customerId);
                return "error";
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
