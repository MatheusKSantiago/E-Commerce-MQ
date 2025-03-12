package org.ecommercemq.pagamento;

import com.rabbitmq.client.Channel;
import org.ecommercemq.config.RabbitMQConfig;
import org.ecommercemq.estoque.domain.model.EstoqueReserva;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Service
public class PagamentoConsumer {
    private final RestTemplate template;
    private final RabbitTemplate rabbitTemplate;
    private final String PAGAMENTO_URL = "http://localhost:8080/api/pagamento";

    public PagamentoConsumer(RabbitTemplate rabbitTemplate) {
        this.template = new RestTemplate();
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.ESTOQUE_RESERVA_QUEUE)
    public void confirmarCompra(EstoqueReserva estoqueReserva, Message message, Channel channel) throws IOException {
        boolean pago = template.getForObject(PAGAMENTO_URL, Boolean.class);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        if(pago){
            channel.basicAck(deliveryTag,false);
        }else{
            channel.basicReject(deliveryTag,false);
        }
        rabbitTemplate.convertAndSend("pagamento.fanout","",new PagamentoPedidoStatus(estoqueReserva.pedido_id,pago));
    }
}
