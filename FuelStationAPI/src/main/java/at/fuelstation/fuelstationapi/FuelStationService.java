package at.fuelstation.fuelstationapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.awt.SystemColor;



import java.util.Optional;

import static java.awt.SystemColor.text;

@Service
public class FuelStationService {
    @Autowired private RabbitTemplate rabbitTemplate;
    public void startDataGatheringJob(String customerId, String queueName, String brokerUrl) {
        System.out.println("customerId: " + customerId); //For Testing Purpose
//geht auch nicht rabbitTemplate.convertAndSend(
//                "exchange-name", "routing-key", customerId);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(brokerUrl);

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ) {
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicPublish("", queueName, null, customerId.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }  // Implement the logic to send a message to the Data Collection Dispatcher via RabbitMQ
    }


    public Optional<String> getInvoice(String customerId) {
        // Implement the logic to fetch the generated PDF invoice from the file system or database
        // Example:
        // Path invoicePath = Paths.get("invoices/" + customerId + ".pdf");
        // if (Files.exists(invoicePath)) {
        //     return Optional.of(Files.readString(invoicePath));
        // } else {
        //     return Optional.empty();
        // }
        return Optional.empty();
    }
}
