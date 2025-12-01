package com.berksozcu.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMQConfiguration {

    @Value("${sr.rabbit.queue.text}")
    private String textQueueName;

    @Value("${sr.rabbit.queue.qr}")
    private String qrQueueName;

    @Value("${sr.rabbit.queue.barcode}")
    private String barcodeQueueName;

    @Value("${sr.rabbit.exchange.name}")
    private String exchangeName;

    @Value("${sr.rabbit.routing.text}")
    private String textRoutingName;

    @Value("${sr.rabbit.routing.qr}")
    private String qrRoutingName;

    @Value("${sr.rabbit.routing.barcode}")
    private String barcodeRoutingName;

    @Bean
    public Queue textQueue() {
        return new Queue(textQueueName, true);
    }

    @Bean
    public Queue qrQueue() {
        return new Queue(qrQueueName, true);
    }

    @Bean
    public Queue barcodeQueue() {
        return new Queue(barcodeQueueName, true);
    }

    @Bean
    public Exchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding textBinding(@Qualifier("textQueue")Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(textRoutingName);
    }

    @Bean
    public Binding barcodeBinding(@Qualifier("barcodeQueue") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(barcodeRoutingName);
    }

    @Bean
    public Binding qrBinding(@Qualifier("qrQueue")Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(qrRoutingName);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    //RabbitTemplate ve AmqpTemplate karışmasın diye kullanırız
    @Primary
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
