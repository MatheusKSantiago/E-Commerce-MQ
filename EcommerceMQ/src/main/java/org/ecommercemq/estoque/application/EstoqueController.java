package org.ecommercemq.estoque.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.ecommercemq.estoque.application.dto.ProdutoDTO;
import org.ecommercemq.estoque.application.dto.mapper.ProdutoMapper;
import org.ecommercemq.estoque.domain.model.Produto;
import org.ecommercemq.estoque.domain.service.EstoqueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/estoque")
@Tag(name = "Estoque API")
public class EstoqueController {

    private EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService){
        this.estoqueService = estoqueService;
    }

    @Operation(
            summary = "Buscar produtos paginados",
            description = "Retorna uma lista de produtos de acordo com a página solicitada."
    )
    @GetMapping("/produtos/{pagina}")
    public ResponseEntity<Page<Produto>> buscarProdutos(
            @Parameter(
                    description = "Número da página de produtos (começando de 0)",
                    required = false,
                    example = "0"
            )@PathVariable("pagina") Optional<Integer> pagina){
        return ResponseEntity.ok(estoqueService.buscarProdutosPaginados(pagina.orElse(0)));
    }

    @Operation(summary = "Salvar produtos")
    @PostMapping("/produtos")
    public ResponseEntity<Void> salvarProdutos(@RequestBody @Valid List<ProdutoDTO> produtos){
        estoqueService.salvarProdutos(produtos.stream().map(ProdutoMapper::fromDto).toList());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
