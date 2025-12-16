package repository;

import entity.CancelOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancelOrderRepository extends JpaRepository<CancelOrderEntity, Long> {
    
}
