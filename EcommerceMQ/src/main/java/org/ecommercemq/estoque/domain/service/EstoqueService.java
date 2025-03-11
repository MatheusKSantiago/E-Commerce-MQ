package org.ecommercemq.estoque.domain.service;

import org.ecommercemq.estoque.domain.exception.EstoqueInsuficienteException;
import org.ecommercemq.estoque.domain.exception.EstoqueProdutoNaoEncontradoException;
import org.ecommercemq.estoque.domain.model.EstoqueReserva;
import org.ecommercemq.estoque.domain.model.Produto;
import org.ecommercemq.estoque.domain.repository.ProdutoRepository;
import org.ecommercemq.pedido.domain.model.Pedido;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class EstoqueService {
    private final int TAMANHO_PAGINA = 10;
    private final ProdutoRepository produtoRepository;
    private final RabbitTemplate rabbitTemplate;

    public EstoqueService(ProdutoRepository produtoRepository, RabbitTemplate rabbitTemplate) {
        this.produtoRepository = produtoRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Page<Produto> buscarProdutosPaginados(int pagina){
        return produtoRepository.findAll(PageRequest.of(pagina,TAMANHO_PAGINA));
    }
    public void salvarProdutos(List<Produto> produtos){
        produtoRepository.saveAll(produtos);
    }

    @RabbitListener(queues = "pedidoQueue")
    @Transactional
    public void processarPedido(Pedido pedido){
        StringBuffer notificarProblemaComPedido = new StringBuffer();
        EstoqueReserva estoqueReserva = new EstoqueReserva();
        estoqueReserva.pedido_id = pedido.getId();
        pedido.getConteudoPedido().forEach((key, value) -> {
                produtoRepository.findProdutoById(key).ifPresentOrElse(
                        p ->{
                            var reservou = p.reservar(value);
                            if(reservou != value){
                                notificarProblemaComPedido.append(
                                        String.format("Produto: %s, Solicitado: %d, Disponível: %d \n",p.getNome(),value,reservou));
                            }
                            estoqueReserva.total_pagar = estoqueReserva.total_pagar.add(p.getPreco().multiply(BigDecimal.valueOf(reservou)));
                            estoqueReserva.conteudo_reservado.put(key,reservou);
                        },
                        ()-> notificarProblemaComPedido.append(String.format("Produto com id: %s, não foi encontrado\n",key))
                );
        });
        rabbitTemplate.convertAndSend("ecommercemq-direct-x","estoqueReservaCreate-routing-key",estoqueReserva);
    }
    @RabbitListener(queues="estoqueReserva-DLQ")
    @Transactional
    public void reporEstoque(EstoqueReserva estoqueReserva){
        estoqueReserva.conteudo_reservado.forEach((key,value)->{
            produtoRepository.findProdutoById(key).ifPresent(p->{
                p.setQuantidade(p.getQuantidade() + value);
                produtoRepository.save(p);
            });
        });
    }
}
