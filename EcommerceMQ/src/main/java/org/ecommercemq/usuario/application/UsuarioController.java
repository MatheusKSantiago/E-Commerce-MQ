package org.ecommercemq.usuario.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.ecommercemq.usuario.application.dto.ClienteCriarDTO;
import org.ecommercemq.usuario.application.dto.UsuarioMapper;
import org.ecommercemq.usuario.domain.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuario")
@Tag(name = "Usuario API")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @Operation(summary = "Criar Cliente")
    @PostMapping("/criar/cliente")
    public ResponseEntity<Void> criarCliente(@RequestBody @Valid ClienteCriarDTO clienteCriarDTO){
        usuarioService.salvarUsuario(UsuarioMapper.fromClienteDTO(clienteCriarDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
