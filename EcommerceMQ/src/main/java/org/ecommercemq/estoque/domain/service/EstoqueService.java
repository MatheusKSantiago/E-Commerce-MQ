package org.ecommercemq.estoque.domain.service;

import org.ecommercemq.config.RabbitMQConfig;
import org.ecommercemq.estoque.domain.exception.EstoqueInsuficienteException;
import org.ecommercemq.estoque.domain.exception.EstoqueProdutoNaoEncontradoException;
import org.ecommercemq.estoque.domain.model.EstoqueReserva;
import org.ecommercemq.estoque.domain.model.EstoqueStatus;
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

    @RabbitListener(queues = RabbitMQConfig.PEDIDOS_QUEUE)
    @Transactional
    public void processarPedido(Pedido pedido){
        StringBuffer observacoes = new StringBuffer();
        EstoqueReserva estoqueReserva = new EstoqueReserva(pedido.getId());

        pedido.getConteudoPedido().forEach((produto_id, quantidade_pedido) -> {
                produtoRepository.findProdutoById(produto_id).ifPresentOrElse(
                        produto ->{
                            reservar(estoqueReserva,produto,quantidade_pedido,observacoes,produto_id);
                        },
                        ()-> observacoes.append(String.format("Produto com id: %s, não foi encontrado\n",produto_id))
                );
        });

        String routingKey = "estoque.reserva.confirm";
        if(estoqueReserva.total_pagar.compareTo(BigDecimal.ZERO) == 0){
            estoqueReserva.status = EstoqueStatus.PEDIDO_NULO;
            routingKey = "estoque.reserva";
        }
        estoqueReserva.observacoes = observacoes.toString();
        rabbitTemplate.convertAndSend("ecommercemq.topic",routingKey,estoqueReserva);

    }
    @RabbitListener(queues= RabbitMQConfig.ESTOQUE_RESERVA_CANCELADA_QUEUE)
    @Transactional
    public void reporEstoque(EstoqueReserva estoqueReserva){
        estoqueReserva.conteudo_reservado.forEach((key,value)->{
            produtoRepository.findProdutoById(key).ifPresent(p->{
                p.setQuantidade(p.getQuantidade() + value);
                produtoRepository.save(p);
            });
        });
    }

    private void reservar(EstoqueReserva estoqueReserva,Produto produto,Integer quantidade_pedido,
                         StringBuffer observacoes,UUID produto_id){

        var reservou = produto.reservar(quantidade_pedido);
        if(reservou != quantidade_pedido){
            estoqueReserva.status = EstoqueStatus.PEDIDO_PARCIAL;
            observacoes.append(
                    String.format("Produto: %s, Solicitado: %d, Disponível: %d \n",produto.getNome(),quantidade_pedido,reservou));
        }
        estoqueReserva.total_pagar = estoqueReserva.total_pagar.add(produto.getPreco().multiply(BigDecimal.valueOf(reservou)));
        estoqueReserva.conteudo_reservado.put(produto_id,reservou);
    }
}
