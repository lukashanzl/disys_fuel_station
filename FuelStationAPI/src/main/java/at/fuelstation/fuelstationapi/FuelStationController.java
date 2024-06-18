package at.fuelstation.fuelstationapi;

import at.fuelstation.fuelstationapi.FuelStationService;
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


@RestController
@RequestMapping("/invoices")
public class FuelStationController {

    private final static String BROKER_URL = "localhost";
    @Autowired
    private FuelStationService invoiceService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<String> generateInvoice(@PathVariable String customerId) throws UnsupportedEncodingException {
        invoiceService.startDataGatheringJob(customerId,"queue_name",BROKER_URL);
        return ResponseEntity.ok("Invoice generation started for customer ID: " + customerId);
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getInvoice(@PathVariable String customerId) {
        // Implement the logic to fetch the generated PDF invoice and return it.
        return invoiceService.getInvoice(customerId)
                .map(invoice -> ResponseEntity.ok(invoice))
                .orElse(ResponseEntity.status(404).body("Invoice not available for customer ID: " + customerId));
    }

}
