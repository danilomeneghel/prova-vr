package api.repository;

import api.entity.SaldoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaldoRepository extends CrudRepository<SaldoEntity, Long > {

}
