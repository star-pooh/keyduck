package org.team1.keyduck.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

    private final RabbitMqProperties rabbitMqProperties;

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(rabbitMqProperties.getQueueName())
                .deadLetterExchange(rabbitMqProperties.getDeadLetterExchangeName())
                .deadLetterRoutingKey(rabbitMqProperties.getDeadLetterRoutingKey())
                .maxLength(100L) // 결제 승인 실패 메시지를 100개까지만 저장
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(rabbitMqProperties.getDeadLetterQueueName(), true);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(rabbitMqProperties.getExchangeName());
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(rabbitMqProperties.getDeadLetterExchangeName());
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange)
                .with(rabbitMqProperties.getRoutingKey());
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange)
                .with(rabbitMqProperties.getDeadLetterRoutingKey());
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMqProperties.getHost());
        connectionFactory.setPort(rabbitMqProperties.getPort());
        connectionFactory.setUsername(rabbitMqProperties.getUsername());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
