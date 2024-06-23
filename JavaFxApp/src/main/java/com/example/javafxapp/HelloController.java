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
    public void initialize() {
        customerIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,4}") || (newValue.length() > 0 && Integer.parseInt(newValue) > 10000)) {
                customerIdField.setText(oldValue);
                statusLabel.setText("Customer ID must be a number between 0 and 10000.");
            } else {
                statusLabel.setText("");
            }
        });
    }

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
