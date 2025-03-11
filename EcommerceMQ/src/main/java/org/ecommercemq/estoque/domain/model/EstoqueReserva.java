package org.ecommercemq.estoque.domain.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EstoqueReserva {

    public UUID pedido_id;
    public BigDecimal total_pagar;
    public Map<UUID,Integer> conteudo_reservado;
    public EstoqueReserva(){
        conteudo_reservado = new HashMap<>();
        total_pagar = BigDecimal.valueOf(0);
    }
}
