package com.example.javafxapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HelloController {

    @FXML
    private TextField customerIdField;

    @FXML
    private Button generateInvoiceButton;

    @FXML
    private Label statusLabel;

    @FXML
    protected void onGenerateInvoiceButtonClick() {
        String customerId = customerIdField.getText();
        if (customerId.isEmpty()) {
            statusLabel.setText("Customer ID cannot be empty.");
            return;
        }

        // HTTP Post with the ID starts
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/invoices/" + customerId))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    Platform.runLater(() -> {
                        statusLabel.setText("Invoice generation started for customer ID: " + customerId);
                    });
                });
    }
}
