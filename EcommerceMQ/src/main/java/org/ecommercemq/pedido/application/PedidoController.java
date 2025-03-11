package org.ecommercemq.pedido.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.ecommercemq.pedido.domain.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pedido")
@Tag(name = "Pedido API")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Fazer Pedido",
    description = "Recebe um Map cuja chave é o ID do produto e o valor é a quantidade desejada")
    @PostMapping
    public ResponseEntity<Void> fazerPedido(@RequestBody @Valid Map<UUID,@Positive Integer> conteudoPedido){
        pedidoService.fazerPedido(conteudoPedido);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
