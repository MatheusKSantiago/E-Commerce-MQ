package org.ecommercemq.notificacao;

import org.ecommercemq.config.RabbitMQConfig;
import org.ecommercemq.estoque.domain.model.EstoqueReserva;
import org.ecommercemq.pagamento.PagamentoPedidoStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificaoService {

    @RabbitListener(queues = RabbitMQConfig.NOTIFICACAO_ESTOQUE_STATUS_QUEUE)
    public void simularNotificacaoEstoque(EstoqueReserva estoqueReserva){
        System.out.println("Pedido: " + estoqueReserva.pedido_id + " confirmado");
        System.out.println("Observações: \n" + estoqueReserva.observacoes);
    }
    @RabbitListener(queues = RabbitMQConfig.NOTIFICACAO_PAGAMENTO_STATUS_QUEUE)
    public void simularNotificacaoPagamento(PagamentoPedidoStatus pagamentoPedidoStatus){
        System.out.printf("Seu pedido ID: %s %s%n",pagamentoPedidoStatus.pedido_id,
                pagamentoPedidoStatus.pago ? "foi pago com sucesso" : "não foi possível realizar o pagamento");
    }
}
