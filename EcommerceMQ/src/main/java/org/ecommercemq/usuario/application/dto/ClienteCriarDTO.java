package org.ecommercemq.usuario.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ClienteCriarDTO {
    @NotBlank
    public String email;
    @NotBlank
    public String nome;
    @NotBlank
    @Min(3)
    public String password;
}
