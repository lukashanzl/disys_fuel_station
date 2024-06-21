package at.fuelstation.fuelstationapi.controller;

import at.fuelstation.fuelstationapi.dto.Invoice;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoiceController {

    private final static String BROKER_URL = "localhost";

    @PostMapping("/invoices/{custId}")
    public Invoice createInvoice(@PathVariable int custId){

        return new Invoice(custId);
    }
}
