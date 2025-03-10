package org.ecommercemq.pedido.domain.model;

import jakarta.persistence.*;

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
    private Map<String, Integer> produtos;

}
