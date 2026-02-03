package com.agribusiness360.backend.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.agribusiness360.backend.repository.ProductRepository;
import com.agribusiness360.backend.model.Product;
import com.agribusiness360.backend.model.ProductStatus;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     *  Retrieves all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     *  Retrieves products by status
     */
    public List<Product> getProductsByStatus(ProductStatus productStatus) {
        return productRepository.findByProductStatus(productStatus);
    }

    /**
     *  Retrieves a product by its ID
    */
   public Product getProductById(Integer id) {
        return productRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Product not found with supplier ID."));
   }

    /**
     *  Retrieves products with a base price within a specific range
     */
    public List<Product> getProductByBasePrice(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByBasePriceBetween(minPrice, maxPrice);
    }

    /**
     *  Saves a new product
     */
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     *  Update existing product
     */
    @Transactional
    public Product updateProduct(Integer id, Product details) {
        Product product = productRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("There is no product registered with the provided ID."));

        product.setBasePrice(details.getBasePrice());
        product.setProductStatus(details.getProductStatus());

        return productRepository.save(product);
    }

    /**
     *  Delete a product
     */
    @Transactional
    public void deleteProduct(Integer id) {
        if(!productRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Product record not found.");
        }

        productRepository.deleteById(id);
    }
}
