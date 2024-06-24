package com.example.station_data_collector.communication;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    public static void send(String text, String queueName, String brokerUrl) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(brokerUrl);


        Address[] addr = new Address[1];
        addr[0] = new Address("localhost",30003);

        try (
                Connection connection = factory.newConnection(addr);
                Channel channel = connection.createChannel()
        ) {
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicPublish("", queueName, null, text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}