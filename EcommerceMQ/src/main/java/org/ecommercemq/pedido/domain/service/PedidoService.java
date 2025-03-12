package org.ecommercemq.pedido.domain.service;

import org.ecommercemq.config.RabbitMQConfig;
import org.ecommercemq.estoque.domain.model.EstoqueReserva;
import org.ecommercemq.pagamento.PagamentoPedidoStatus;
import org.ecommercemq.pedido.domain.model.Pedido;
import org.ecommercemq.pedido.domain.model.PedidoStatus;
import org.ecommercemq.pedido.domain.repository.PedidoRepository;
import org.ecommercemq.usuario.domain.model.Usuario;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final RabbitTemplate rabbitTemplate;

    public PedidoService(PedidoRepository pedidoRepository, RabbitTemplate rabbitTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void fazerPedido(Map<UUID,Integer> conteudoPedido){
        var pedido = new Pedido();
        pedido.setConteudoPedido(conteudoPedido);
        var cliente = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        pedido.setCliente_id(cliente.getId());

        pedidoRepository.save(pedido);
        rabbitTemplate.convertAndSend("ecommercemq.direct","pedido.create",pedido);
    }


    @RabbitListener(queues = RabbitMQConfig.PAGAMENTO_STATUS_QUEUE)
    @Transactional
    public void pagamentoStatus(PagamentoPedidoStatus pagamentoPedidoStatus){
        pedidoRepository.findById(pagamentoPedidoStatus.pedido_id).ifPresent(pedido -> {
            var statusHistorico = pedido.getStatus();
            if(pagamentoPedidoStatus.pago){
                statusHistorico.add(PedidoStatus.PAGAMENTO_SUCESSO);
            }else{
                statusHistorico.add(PedidoStatus.PAGAMENTO_FALHA);
            }
            pedidoRepository.save(pedido);
        });
    }
    @RabbitListener(queues = RabbitMQConfig.ESTOQUE_RESERVA_STATUS_QUEUE)
    @Transactional
    public void estoqueStatus(EstoqueReserva estoqueReserva){
        pedidoRepository.findById(estoqueReserva.pedido_id).ifPresent(pedido -> {
            var statusHistorico = pedido.getStatus();
            statusHistorico.add(PedidoStatus.valueOf(estoqueReserva.status.name()));
            pedidoRepository.save(pedido);
        });
    }

}
