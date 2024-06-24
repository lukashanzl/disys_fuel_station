package com.example.data_collection_dispatcher.service;

import com.example.data_collection_dispatcher.communication.Producer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DataCollectionDispatcherServiceTests {

    @Test
    void testWithCorrectData(){
        DataCollectionDispatcherService dcdService = new DataCollectionDispatcherService("testIn","testOut","testBroker");

        //Arrange
        String input = "1";

        //Act
        String result = dcdService.executeInternal(input);

        //Assert
        Assertions.assertEquals(result,"ok");
    }
}