package com.example.data_collection_receiver.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class DataCollectionReceiverServiceTest {

    @Test
    void testWithCorrectData(){
        DataCollectionReceiverService dcrService = new DataCollectionReceiverService("testIn","testOut","testBroker");

        //Arrange
        String input = "ok";

        //Act
        String result = dcrService.executeInternal(input);

        //Assert
        Assertions.assertEquals(result,"ok");
    }

    @Test
    void testWithCorrectDataFromStationDb(){
        DataCollectionReceiverService dcrService = new DataCollectionReceiverService("testIn","testOut","testBroker");

        //Arrange
        String input = "1,100,3";

        //Act
        String result = dcrService.executeInternal(input);

        //Assert
        Assertions.assertEquals(result,"ok");
    }
  
}