package com.agribusiness360.backend.controller;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.agribusiness360.backend.model.Product;
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
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    /**
     *  Search products by status
     */
    @GetMapping("/product-status")
    public ResponseEntity<List<Product>> getProductsByStatus(@RequestParam ProductStatus status) {
        List<Product> products = productService.getProductsByStatus(status);

        return ResponseEntity.ok(products);
    }

    /**
     *  Search for a product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    /**
     *  Search for a product based on price
     */
    @GetMapping("/specific-range")
    public ResponseEntity<List<Product>> getProductsBySpecificRange(@RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice) {
        List<Product> products = productService.getProductByBasePrice(minPrice, maxPrice);

        return ResponseEntity.ok(products);
    }

    /**
     *  Save a new product
     */
    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        Product newProduct = productService.saveProduct(product);

        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    /**
     *  Update a product
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);

        return ResponseEntity.ok(updatedProduct);
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
