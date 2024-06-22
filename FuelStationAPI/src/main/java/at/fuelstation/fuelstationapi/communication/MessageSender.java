package at.fuelstation.fuelstationapi.communication;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public void send(String customerId) {
        rabbitTemplate.convertAndSend(queue.getName(), customerId);
        System.out.println(" [x] Sent '" + customerId + "'");
    }
}
