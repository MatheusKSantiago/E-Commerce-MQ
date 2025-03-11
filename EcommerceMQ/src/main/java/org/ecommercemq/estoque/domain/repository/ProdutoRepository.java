package org.ecommercemq.estoque.domain.repository;

import jakarta.persistence.LockModeType;
import org.ecommercemq.estoque.domain.model.Produto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Produto> findProdutoById(UUID id);
}
