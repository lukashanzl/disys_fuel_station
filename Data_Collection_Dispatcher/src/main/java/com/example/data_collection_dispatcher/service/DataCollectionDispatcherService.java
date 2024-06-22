package com.example.data_collection_dispatcher.service;

import com.example.data_collection_dispatcher.DTOs.Station;
import com.example.data_collection_dispatcher.communication.Producer;
import java.sql.*;
import java.util.Arrays;
import java.util.UUID;

public class DataCollectionDispatcherService extends BaseService {

    private final static String DB_CONNECTION = "jdbc:postgresql://localhost:30002/postgres?user=postgres&password=postgres";

    private final static String BROKER_URL = "localhost";

    private final String id;

    public DataCollectionDispatcherService(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);

        this.id = UUID.randomUUID().toString();

        System.out.println("DataCollectionDispatcherService (" + this.id + ") started... ");
    }

    @Override
    protected String executeInternal(String input) {
        String jobId = UUID.randomUUID().toString();

        System.out.println("DataCollectionDispatcherService (" + jobId + ") executing " + input);

        String dcrInput = "DataCollectionDispatcher started Job with id: " + jobId;
        Producer.send(dcrInput,"DataCollectionReceiver",BROKER_URL);
        System.out.println("Message sent to the DataCollectionReceiver"+dcrInput);

        try(Connection conn = connect()){
            String sql = "SELECT id, db_url, lat, lng FROM station";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();

            int idx = 0;

            while (rs.next()){
                //dbResult[idx] = new Station(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4));

                // send data to rabbitMQ queue
                String queueInput = input + "," + rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4);
                Producer.send(queueInput,"StationDataCollector",BROKER_URL);
                System.out.println("Message sent to the Station Data Collector: "+queueInput);
            }
        }catch (SQLException e){
            return "error";
        }

        return "ok";
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION);
    }
}
