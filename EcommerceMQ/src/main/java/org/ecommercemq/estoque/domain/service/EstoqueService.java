package org.ecommercemq.estoque.domain.service;

import org.ecommercemq.estoque.domain.model.Produto;
import org.ecommercemq.estoque.domain.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EstoqueService {
    private final int TAMANHO_PAGINA = 10;
    private final ProdutoRepository produtoRepository;

    public EstoqueService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    public Page<Produto> buscarProdutosPaginados(int pagina){
        return produtoRepository.findAll(PageRequest.of(pagina,TAMANHO_PAGINA));
    }
    public void salvarProduto(Produto produto){
        produtoRepository.save(produto);
    }
}
