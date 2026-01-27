package com.agribusiness360.backend.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agribusiness360.backend.model.Product;
import com.agribusiness360.backend.model.ProductStatus;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * Filter all products by availability status.
     */
    List<Product> findByProductStatus(ProductStatus productStatus);

    /**
     * Search products with base price within 
     * a specific range
     */
    List<Product> findByBasePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}
