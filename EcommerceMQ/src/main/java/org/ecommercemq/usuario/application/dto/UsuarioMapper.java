package org.ecommercemq.usuario.application.dto;

import org.ecommercemq.usuario.domain.model.TiposUsuario;
import org.ecommercemq.usuario.domain.model.Usuario;

public class UsuarioMapper {

    public static Usuario fromClienteDTO(ClienteCriarDTO clienteCriarDTO){
        return new Usuario(clienteCriarDTO.nome, clienteCriarDTO.email,clienteCriarDTO.password, TiposUsuario.CLIENTE);
    }
}
