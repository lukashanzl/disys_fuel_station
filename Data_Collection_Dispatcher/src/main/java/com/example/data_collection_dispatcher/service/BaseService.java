package com.example.data_collection_dispatcher.service;

import com.example.data_collection_dispatcher.communication.Consumer;
import com.example.data_collection_dispatcher.communication.Producer;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public abstract class BaseService {
    protected final String inDestination;
    protected final String outDestination;
    protected final String brokerUrl;

    public BaseService(String inDestination, String outDestination, String brokerUrl) {
        this.inDestination = inDestination;
        this.outDestination = outDestination;
        this.brokerUrl = brokerUrl;
    }

    public void run() throws IOException, TimeoutException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String input = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String output = executeInternal(input);
            Producer.send(output, outDestination, brokerUrl);
        };
        Consumer.receive(inDestination, 10000, brokerUrl, deliverCallback);
    }

    protected abstract String executeInternal(String input);
}
