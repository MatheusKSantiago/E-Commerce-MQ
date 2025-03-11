package org.ecommercemq.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
    public Queue estoqueReservaQueue(){
        return new Queue("estoqueReservaQueue",true);
    }
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("ecommercemq-direct-x");
    }
    @Bean
    public Binding pedidoDirectBinding(){
        return BindingBuilder.bind(pedidosQueue()).to(directExchange()).with("pedidoCreate-routing-key");
    }
    @Bean
    public Binding estoqueReservaDirectBinding(){
        return BindingBuilder.bind(estoqueReservaQueue()).to(directExchange()).with("estoqueReservaCreate-routing-key");
    }
}
