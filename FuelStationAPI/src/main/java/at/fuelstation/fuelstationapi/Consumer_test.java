package at.fuelstation.fuelstationapi;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


//see if the queue Messages arrive
@Component
public class Consumer_test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer_test.class);

    private RabbitTemplate rabbitTemplate;
    @RabbitListener(queues = "queue-name")
    public void receiveMessage(String message)
    {
        LOGGER.info(String.format("Message received -> %s", message));
    }

}
