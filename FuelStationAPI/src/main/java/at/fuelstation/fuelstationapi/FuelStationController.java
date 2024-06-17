package at.fuelstation.fuelstationapi;

import at.fuelstation.fuelstationapi.FuelStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
public class FuelStationController {
    @Autowired
    private FuelStationService invoiceService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<String> generateInvoice(@PathVariable String customerId) {
        invoiceService.startDataGatheringJob(customerId);
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
