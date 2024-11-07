package api.repository;

import api.entity.TransacaoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends CrudRepository<TransacaoEntity, Long > {

    List<TransacaoEntity> findAll();

}
