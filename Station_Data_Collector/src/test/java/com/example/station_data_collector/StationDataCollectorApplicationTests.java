package com.example.station_data_collector;

import com.example.station_data_collector.communication.Consumer;
import com.example.station_data_collector.communication.Producer;
import com.example.station_data_collector.service.StationDataCollectorService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class StationDataCollectorApplicationTests {

    private TestableStationDataCollectorService service;

    @BeforeEach
    void setUp() {
        service = new TestableStationDataCollectorService("StationDataCollector", "DataCollectionReceiver", "localhost");
    }

    @Test
    void testExecuteInternal_withValidData() throws SQLException {
        // Mocking the database connection
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("id")).thenReturn("chargeId");
        when(mockResultSet.getDouble("kwh")).thenReturn(100.0);
        when(mockResultSet.getString("customer_id")).thenReturn("customerId");

        try (MockedStatic<DriverManager> driverManagerMockedStatic = mockStatic(DriverManager.class)) {
            driverManagerMockedStatic.when(() -> DriverManager.getConnection(anyString())).thenReturn(mockConnection);

            // Mocking Producer.send
            try (MockedStatic<Producer> producerMockedStatic = mockStatic(Producer.class)) {
                ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
                producerMockedStatic.when(() -> Producer.send(messageCaptor.capture(), anyString(), anyString())).thenAnswer(invocation -> null);

                String input = "1,station1,localhost,12.34,56.78";
                String result = service.executeInternal(input);

                assertEquals("ok", result);
                assertEquals("station1,customerId,chargeId,100.0;", messageCaptor.getValue());
            }
        }
    }

    @Test
    void testExecuteInternal_withNoData() throws SQLException {
        // Mocking the database connection
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        try (MockedStatic<DriverManager> driverManagerMockedStatic = mockStatic(DriverManager.class)) {
            driverManagerMockedStatic.when(() -> DriverManager.getConnection(anyString())).thenReturn(mockConnection);

            // Mocking Producer.send
            try (MockedStatic<Producer> producerMockedStatic = mockStatic(Producer.class)) {
                ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
                producerMockedStatic.when(() -> Producer.send(messageCaptor.capture(), anyString(), anyString())).thenAnswer(invocation -> null);

                String input = "1,station1,localhost,12.34,56.78";
                String result = service.executeInternal(input);

                assertEquals("error", result);
                assertEquals("output", messageCaptor.getValue());
            }
        }
    }

    private static class TestableStationDataCollectorService extends StationDataCollectorService {
        public TestableStationDataCollectorService(String inDestination, String outDestination, String brokerUrl) {
            super(inDestination, outDestination, brokerUrl);
        }

        @Override
        public String executeInternal(String input) {
            return super.executeInternal(input);
        }
    }
}
