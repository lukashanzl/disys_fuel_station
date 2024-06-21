package at.fuelstation.fuelstationapi;

import at.fuelstation.fuelstationapi.communication.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.awt.SystemColor;



import java.util.Optional;

import static java.awt.SystemColor.text;

@Service
public class FuelStationService {

    @Autowired
    private MessageSender messageSender;
    public void startDataGatheringJob(String customerId) {
        messageSender.send(customerId);
    }


    public Optional<String> getInvoice(String customerId) {
        // fetch generated Invoice
        return Optional.empty();
    }
}
