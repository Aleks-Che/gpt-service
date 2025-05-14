package Aleks.Che.gpt_service_back.repository;

import Aleks.Che.gpt_service_back.model.order.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByUserId(Long id);
}
