package org.ecommercemq.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory){
        var template = new RabbitTemplate(factory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public Queue pedidosQueue(){
        return new Queue("pedidoQueue",true);
    }
    @Bean
    public Queue dlqEstoqueReserva(){
        return new Queue("estoqueReserva-DLQ",true);
    }

    @Bean
    public Queue estoqueReservaQueue(){
        return QueueBuilder
                .durable("estoqueReservaQueue")
                .ttl(10 * 1000) // 3 minutos
                .deadLetterExchange("estoque-reserva-dlx")
                .deadLetterRoutingKey("estoque-reserva-dlq-routing-key")
                .build();
    }
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("ecommercemq-direct-x");
    }
    @Bean
    public DirectExchange dlxEstoqueDirectExchange(){
        return new DirectExchange("estoque-reserva-dlx");
    }

    @Bean
    public Binding pedidoDirectBinding(){
        return BindingBuilder.bind(pedidosQueue()).to(directExchange()).with("pedidoCreate-routing-key");
    }
    @Bean
    public Binding estoqueReservaDirectBinding(){
        return BindingBuilder.bind(estoqueReservaQueue()).to(directExchange()).with("estoqueReservaCreate-routing-key");
    }
    @Bean
    public Binding estoqueReservaDlqBinding(){
        return BindingBuilder.bind(dlqEstoqueReserva()).to(dlxEstoqueDirectExchange()).with("estoque-reserva-dlq-routing-key");
    }
}
