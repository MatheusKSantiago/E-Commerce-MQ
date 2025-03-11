package org.ecommercemq.estoque.domain.exception;

import java.util.UUID;

public class EstoqueInsuficienteException extends Exception{
    private UUID pedido;
    private String message;
    public EstoqueInsuficienteException(String message) {
        super(message);
    }

    public EstoqueInsuficienteException(String message, UUID pedido) {
        this.message = message;
        this.pedido = pedido;
    }
}
