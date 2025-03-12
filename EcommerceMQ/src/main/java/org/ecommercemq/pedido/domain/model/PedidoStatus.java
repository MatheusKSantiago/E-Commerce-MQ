package org.ecommercemq.pedido.domain.model;

public enum PedidoStatus {
    INICIO,
    PEDIDO_PARCIAL,
    PEDIDO_INTEGRAL,
    PEDIDO_NULO,
    PAGAMENTO_FALHA,
    PAGAMENTO_SUCESSO
}
