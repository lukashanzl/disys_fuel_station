package com.example.data_collection_receiver.communication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    public static void send(String text, String queueName, String brokerUrl) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(brokerUrl);

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ) {
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicPublish("", queueName, null, text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}