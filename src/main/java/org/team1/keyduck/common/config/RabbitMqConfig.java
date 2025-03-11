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

    /**
     * queue와 exchange를 binding하고 routing key를 이용하여 빈 생성
     * <p>
     * 즉, exchange에 queue를 등록
     */
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

//    @Bean
//    public MessageConverter jackson2JsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        //        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return new RabbitTemplate(connectionFactory);
    }
}
