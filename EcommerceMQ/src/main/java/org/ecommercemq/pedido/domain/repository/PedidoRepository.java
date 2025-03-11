package org.ecommercemq.pedido.domain.repository;

import org.ecommercemq.pedido.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
}
