package org.ecommercemq.pedido.domain.model;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;
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
    @ElementCollection
    @CollectionTable(name = "pedido_status_historico", joinColumns = @JoinColumn(name = "pedido_id"))
    private List<PedidoStatus> status;

    private ZonedDateTime feitoEm;
    @PrePersist
    public void pedidoInit(){
        status = List.of(PedidoStatus.INICIO);
        feitoEm = ZonedDateTime.now();
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



    public void setCliente_id(UUID cliente_id) {
        this.cliente_id = cliente_id;
    }

    public void setConteudoPedido(Map<UUID, Integer> conteudoPedido) {
        this.conteudoPedido = conteudoPedido;
    }

    public List<PedidoStatus> getStatus() {
        return status;
    }

    public void setStatus(List<PedidoStatus> status) {
        this.status = status;
    }
}
