package org.ecommercemq.pedido.domain.model;

import jakarta.persistence.*;
import org.ecommercemq.usuario.domain.model.PedidoStatus;

import java.util.Map;
import java.util.UUID;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID cliente_id;
    @ElementCollection
    @CollectionTable(name = "pedido_produtos", joinColumns = @JoinColumn(name = "pedido_id"))
    @MapKeyColumn(name = "produto_id")
    @Column(name = "quantidade")
    private Map<UUID, Integer> conteudoPedido;

    @Enumerated(value = EnumType.STRING)
    private PedidoStatus status;
    @PrePersist
    public void statusInicio(){
        this.status = PedidoStatus.ESPERANDO;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCliente_id() {
        return cliente_id;
    }

    public Map<UUID, Integer> getConteudoPedido() {
        return conteudoPedido;
    }

    public PedidoStatus getStatus() {
        return status;
    }

    public void setCliente_id(UUID cliente_id) {
        this.cliente_id = cliente_id;
    }

    public void setConteudoPedido(Map<UUID, Integer> conteudoPedido) {
        this.conteudoPedido = conteudoPedido;
    }

    public void setStatus(PedidoStatus status) {
        this.status = status;
    }
}
