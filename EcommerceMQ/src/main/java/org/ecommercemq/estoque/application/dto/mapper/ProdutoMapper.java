package org.ecommercemq.estoque.application.dto.mapper;

import org.ecommercemq.estoque.application.dto.ProdutoDTO;
import org.ecommercemq.estoque.domain.model.Produto;

public class ProdutoMapper {

    public static Produto fromDto(ProdutoDTO dto){
        var produto = new Produto();
        produto.setDescricao(dto.descricao);
        produto.setNome(dto.nome);
        produto.setPreco(dto.preco);
        produto.setQuantidade(dto.quantidade);
        return produto;
    }
}
