package at.fuelstation.fuelstationapi.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class RabbitMQConfig {
        @Bean
        public Queue dataCollectionDispatcherQueue() {
            return new Queue("DataCollectionDispatcher", false);
        }
}
