package org.ecommercemq.pagamento;

import java.util.UUID;

public class PagamentoPedidoStatus {

    public PagamentoPedidoStatus(UUID pedido_id, boolean pago) {
        this.pedido_id = pedido_id;
        this.pago = pago;
    }

    public UUID pedido_id;
    public boolean pago;
}
