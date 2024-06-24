package com.example.station_data_collector.communication;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void receive(String queueName, long timeout, String brokerUrl, DeliverCallback deliverCallback) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(brokerUrl);

        Address[] addr = new Address[1];
        addr[0] = new Address("localhost",30003);

        Connection connection = factory.newConnection(addr);
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}