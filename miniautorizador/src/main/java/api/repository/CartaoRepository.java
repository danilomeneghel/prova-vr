package api.repository;

import api.entity.CartaoEntity;
import api.enums.CartaoStatus;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartaoRepository extends CrudRepository<CartaoEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CartaoEntity> findByNumeroCartao(String numeroCartao);

    List<CartaoEntity> findAllByOrderByNumeroCartaoAsc();

    List<CartaoEntity> findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus status);

    CartaoEntity findTopByOrderByIdDesc();
}
