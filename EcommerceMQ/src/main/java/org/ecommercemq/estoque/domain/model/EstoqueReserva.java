package org.ecommercemq.estoque.domain.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EstoqueReserva {

    public UUID pedido_id;
    public BigDecimal total_pagar;
    public EstoqueStatus status;
    public String observacoes;
    public Map<UUID,Integer> conteudo_reservado;
    public EstoqueReserva(UUID pedido_id){
        this.pedido_id = pedido_id;
        conteudo_reservado = new HashMap<>();
        total_pagar = BigDecimal.valueOf(0);
        status = EstoqueStatus.PEDIDO_INTEGRAL;
    }
}
