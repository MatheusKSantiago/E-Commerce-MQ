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
    public final static String PEDIDOS_QUEUE = "pedidos";
    public final static String ESTOQUE_RESERVA_QUEUE = "estoque.reserva";
    public final static String ESTOQUE_RESERVA_CANCELADA_QUEUE = "estoque.reserva.cancelada";
    public final static String ESTOQUE_RESERVA_STATUS_QUEUE = "estoque.reserva.status";
    public final static String PAGAMENTO_STATUS_QUEUE = "pagamento.status";
    public final static String NOTIFICACAO_ESTOQUE_STATUS_QUEUE = "notificacao.estoque.status";
    public final static String NOTIFICACAO_PAGAMENTO_STATUS_QUEUE = "notificacao.pagamento.status";

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
        return new Queue(PEDIDOS_QUEUE,true);
    }
    @Bean
    public Queue estoqueReservaCancelada(){
        return new Queue(ESTOQUE_RESERVA_CANCELADA_QUEUE,true);
    }

    @Bean
    public Queue estoqueReservaQueue(){
        return QueueBuilder
                .durable(ESTOQUE_RESERVA_QUEUE)
                .ttl(40 * 1000)
                .deadLetterExchange("estoque.reserva-dlx")
                .deadLetterRoutingKey("estoque-reserva-dlq-routing-key")
                .build();
    }
    @Bean
    public Queue estoqueStatus(){
        return new Queue(ESTOQUE_RESERVA_STATUS_QUEUE,true);
    }
    @Bean
    public Queue pagamentoStatus(){
        return new Queue(PAGAMENTO_STATUS_QUEUE,true);
    }
    @Bean
    public Queue notificacaoEstoqueStatus(){
        return new Queue(NOTIFICACAO_ESTOQUE_STATUS_QUEUE,true);
    }
    @Bean
    public Queue notificacaoPagamentoStatus(){
        return new Queue(NOTIFICACAO_PAGAMENTO_STATUS_QUEUE,true);
    }
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("ecommercemq.direct");
    }
    @Bean
    public DirectExchange dlxEstoqueDirectExchange(){
        return new DirectExchange("estoque.reserva-dlx");
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("ecommercemq.topic");
    }
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("pagamento.fanout");
    }

    @Bean
    public Binding pedidoDirectBinding(){
        return BindingBuilder.bind(pedidosQueue()).to(directExchange()).with("pedido.create");
    }

    @Bean
    public Binding estoqueStatusTopicBinding(){
        return BindingBuilder.bind(estoqueStatus()).to(topicExchange()).with("estoque.reserva.#");
    }
    @Bean
    public Binding notificacaoEstoqueTopicBinding(){
        return BindingBuilder.bind(notificacaoEstoqueStatus()).to(topicExchange()).with("estoque.reserva.#");
    }

    @Bean
    public Binding estoqueReservaTopicBinding(){
        return BindingBuilder.bind(estoqueReservaQueue()).to(topicExchange()).with("estoque.reserva.confirm");
    }

    @Bean
    public Binding estoqueReservaDlqBinding(){
        return BindingBuilder.bind(estoqueReservaCancelada()).to(dlxEstoqueDirectExchange()).with("estoque-reserva-dlq-routing-key");
    }

    @Bean
    public Binding estoqueStatusFanoutBinding(){
        return BindingBuilder.bind(pagamentoStatus()).to(fanoutExchange());
    }
    @Bean
    public Binding notificacaoPagamentoStatusFanoutBinding(){
        return BindingBuilder.bind(notificacaoPagamentoStatus()).to(fanoutExchange());
    }
}