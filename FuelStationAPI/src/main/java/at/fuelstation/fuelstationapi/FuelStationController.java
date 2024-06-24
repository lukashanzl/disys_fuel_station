package at.fuelstation.fuelstationapi;

import at.fuelstation.fuelstationapi.FuelStationService;
import at.fuelstation.fuelstationapi.communication.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import at.fuelstation.fuelstationapi.FuelStationService;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/invoices")
public class FuelStationController {

    private final static String BROKER_URL = "localhost";

    @Autowired
    private FuelStationService invoiceService;

    @PostMapping("/{customerId}")
    public ResponseEntity<String> generateInvoice(@PathVariable String customerId) throws UnsupportedEncodingException {
        invoiceService.startDataGatheringJob(customerId);
        return ResponseEntity.ok("Invoice generation started for customer ID: " + customerId);
    }


    @GetMapping("/{customerId}")
    public String getInvoice(@PathVariable String customerId) {
        return "jobId.pdf";
    }

}
