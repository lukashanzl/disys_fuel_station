package com.example.data_collection_dispatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class DataCollectionDispatcherApplication {

    static RabbitMQConsumer dispatcher=new RabbitMQConsumer();

    public static void main(String[] args) throws IOException, TimeoutException {
        dispatcher.start();
    }
}
