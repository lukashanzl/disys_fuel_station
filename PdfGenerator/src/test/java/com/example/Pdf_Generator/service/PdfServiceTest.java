package com.example.Pdf_Generator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class PdfServiceTest {

    @Test
    void testWithCorrectDataFromStationDB(){
        PdfService pdfService = new PdfService("testIn","testOut","testBroker");

        //Arrange
        String input = "100,3";

        //Act
        String result = pdfService.executeInternal(input);

        //
        Assertions.assertEquals(result, "ok");
    }

    @Test
    void testWithCorrectData(){
        PdfService pdfService = new PdfService("testIn","testOut","testBroker");

        //Arrange
        String input = "ok";

        //Act
        String result = pdfService.executeInternal(input);

        //
        Assertions.assertEquals(result, "ok");
    }
  
}