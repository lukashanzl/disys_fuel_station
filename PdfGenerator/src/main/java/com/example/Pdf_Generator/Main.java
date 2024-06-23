package com.example.Pdf_Generator;

import com.example.Pdf_Generator.service.PdfService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    private static final String BROKER_URL = "localhost";

    public static void main(String[] args) throws IOException, TimeoutException {
        PdfService dcdService = new PdfService("PdfGenerator", "", BROKER_URL);
        dcdService.run();
    }
}