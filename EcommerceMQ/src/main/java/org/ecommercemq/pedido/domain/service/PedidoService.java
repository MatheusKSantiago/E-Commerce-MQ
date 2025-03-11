package org.ecommercemq.pedido.domain.service;

import org.ecommercemq.pedido.domain.model.Pedido;
import org.ecommercemq.pedido.domain.repository.PedidoRepository;
import org.ecommercemq.usuario.domain.model.Usuario;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        rabbitTemplate.convertAndSend("ecommercemq-direct-x","pedidoCreate-routing-key",pedido);
    }
}
