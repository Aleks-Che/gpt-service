package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.model.product.Product;
import Aleks.Che.gpt_service_back.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ResponseBody
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    @GetMapping("/search/{name}")
    public List<Product> search(@PathVariable String name) {
        return productService.search(name);
    }

    @GetMapping("/cat/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping(value = "/add")
    public Product addProduct(@RequestBody Product product) {
         productService.addProduct(product);
        return product;
    }
}
