package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.model.product.Product;
import Aleks.Che.gpt_service_back.model.product.ProductCategory;
import Aleks.Che.gpt_service_back.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getProductsByCategory(String category) {
        return new ArrayList<>(productRepository.findByCategory(ProductCategory.valueOf(category)));
    }

    public Product addProduct(Product product) {
        productRepository.save(product);
        return product;
    }

    public List<Product> search(String name) {
        return new ArrayList<>(productRepository.findByNameContaining(name));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(productRepository.findAll());
    }
}
