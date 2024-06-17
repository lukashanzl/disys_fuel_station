package com.example.data_collection_dispatcher;

import com.rabbitmq.client.*;

public class RabbitMQConsumer {

    public static void receive(String queueName, String brokerUrl, String username, String password) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(brokerUrl);
        factory.setUsername(username);
        factory.setPassword(password);

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            boolean durable = true;
            channel.queueDeclare(queueName, durable, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                // Fügen Sie hier Ihre Logik zur Nachrichtenverarbeitung ein
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to receive message: " + e.getMessage());
        }
    }

    public void start() {
        String queueName = "queue-name";
        String brokerUrl = "localhost";
        String username = "guest";
        String password = "guest";
        receive(queueName, brokerUrl, username, password);
    }
}
