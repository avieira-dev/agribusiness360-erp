package com.agribusiness360.backend.controller;

import java.math.BigDecimal;
import java.util.List;
import com.agribusiness360.backend.dto.ProductRequestDTO;
import com.agribusiness360.backend.dto.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.agribusiness360.backend.model.ProductStatus;
import com.agribusiness360.backend.service.ProductService;

@RestController
@RequestMapping("/api/products")
/** SECURITY CONFIGURATION (CORS):
 *  - Currently configured to accept requests from the Vue/Vite Frontend 
 *  (localhost:5173).
 *  - IMPORTANT: For production environments, 
 *  replace "http://localhost:5173" with the official URL where your 
 *  Frontend is hosted.
 */
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     *  Search all products
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     *  Search products by status
     */
    @GetMapping("/product-status")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByStatus(@RequestParam ProductStatus status) {
        return ResponseEntity.ok(productService.getProductsByStatus(status));
    }

    /**
     *  Search for a product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     *  Search for a product based on price
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponseDTO>> getProductsBySpecificRange(@RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(productService.getProductByBasePrice(minPrice, maxPrice));
    }

    /**
     *  Update a product
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Integer id, @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    /**
     *  Delete a product
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}
