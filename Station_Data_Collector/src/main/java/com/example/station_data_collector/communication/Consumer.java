package com.example.station_data_collector.communication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void receive(String queueName, long timeout, String brokerUrl, DeliverCallback deliverCallback) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(brokerUrl);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}