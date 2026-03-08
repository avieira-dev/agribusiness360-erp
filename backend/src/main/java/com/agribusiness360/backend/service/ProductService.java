package com.agribusiness360.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import com.agribusiness360.backend.dto.ProductRequestDTO;
import com.agribusiness360.backend.dto.ProductResponseDTO;
import com.agribusiness360.backend.exception.BusinessException;
import com.agribusiness360.backend.exception.ResourceNotFoundException;
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
     *  Convert entity to DTO
     */
    private ProductResponseDTO toResponse(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getProductStatus(),
                product.getBasePrice()
        );
    }

    /**
     *  Convert DTO to entity
     */
    private Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();

        product.setProductStatus(dto.productStatus());
        product.setBasePrice(dto.basePrice());

        return product;
    }

    /**
     *  Performs business validations on product data
     */
   private void validateProductData(ProductRequestDTO dto) {

        if(dto.basePrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Base price cannot be negative.");
        }
   }

    /**
     *  Retrieves all products
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves products by status
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByStatus(ProductStatus productStatus) {
        return productRepository.findByProductStatus(productStatus).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves a product by its ID
    */
    @Transactional(readOnly = true)
   public ProductResponseDTO getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no product registered with the provided ID."));

        return toResponse(product);
   }

    /**
     *  Retrieves products with a base price within a specific range
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductByBasePrice(BigDecimal minPrice, BigDecimal maxPrice) {
        if(minPrice == null || maxPrice == null) {
            throw new BusinessException("Price values cannot be null.");
        }

        BigDecimal start = minPrice.min(maxPrice);
        BigDecimal end = minPrice.max(maxPrice);

        return productRepository.findByBasePriceBetween(start, end).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Saves a new product
     */
    @Transactional
    public ProductResponseDTO saveProduct(ProductRequestDTO dto) {
        validateProductData(dto);

        Product newProduct = toEntity(dto);

        return toResponse(productRepository.save(newProduct));
    }

    /**
     *  Update existing product
     */
    @Transactional
    public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO dto) {
        if(!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("There is no product registered with the provided ID.");
        }

        validateProductData(dto);

        Product updatedProduct = toEntity(dto);

        updatedProduct.setId(id);

        return toResponse(productRepository.save(updatedProduct));
    }

    /**
     *  Delete a product
     */
    @Transactional
    public void deleteProduct(Integer id) {
        if(!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete: Product record not found.");
        }

        productRepository.deleteById(id);
    }
}
