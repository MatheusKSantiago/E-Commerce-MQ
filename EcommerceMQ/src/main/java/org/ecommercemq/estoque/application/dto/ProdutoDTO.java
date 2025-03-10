package org.ecommercemq.estoque.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProdutoDTO{
    @NotBlank(message = "descrição não pode ser nula ou vazia")
    public String descricao;
    @NotBlank(message = "nome não pode ser nulo ou vazio")
    public String nome;
    @NotNull(message = "preço deve ser fornecido")
    @DecimalMin(value = "0.01")
    public BigDecimal preco;
    @NotNull(message = "quantidade deve ser fornecida")
    @Min(1)
    public Integer quantidade;
}
