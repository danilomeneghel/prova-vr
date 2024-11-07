package api.repository;

import api.entity.CartaoEntity;
import api.enums.CartaoStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartaoRepository extends CrudRepository<CartaoEntity, Long > {

    Optional<CartaoEntity> findByNumeroCartao(String numeroCartao );

    List<CartaoEntity> findAllByOrderByNumeroCartaoAsc();

    List<CartaoEntity> findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus status );

}
