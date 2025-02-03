package Aleks.Che.gpt_service_back.repository;

import Aleks.Che.gpt_service_back.model.product.Product;
import Aleks.Che.gpt_service_back.model.product.ProductCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByCategory(ProductCategory category);
    List<Product> findByNameContaining(String name);
    Optional<Product> findById(Long id);
    List<Product> findAll();
}
