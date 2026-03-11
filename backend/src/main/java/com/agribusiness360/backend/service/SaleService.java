package com.agribusiness360.backend.service;

import com.agribusiness360.backend.dto.ItemRequestDTO;
import com.agribusiness360.backend.dto.SaleRequestDTO;
import com.agribusiness360.backend.dto.SaleResponseDTO;
import com.agribusiness360.backend.dto.TradedResponseDTO;
import com.agribusiness360.backend.exception.BusinessException;
import com.agribusiness360.backend.exception.ResourceNotFoundException;
import com.agribusiness360.backend.model.PaymentMethod;
import com.agribusiness360.backend.model.Product;
import com.agribusiness360.backend.model.ProductStatus;
import com.agribusiness360.backend.model.RuralProperty;
import com.agribusiness360.backend.model.Sale;
import com.agribusiness360.backend.model.Traded;
import com.agribusiness360.backend.model.TradedId;
import com.agribusiness360.backend.repository.ProductRepository;
import com.agribusiness360.backend.repository.RuralPropertyRepository;
import com.agribusiness360.backend.repository.SaleRepository;
import com.agribusiness360.backend.repository.TradedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private RuralPropertyRepository ruralPropertyRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TradedRepository tradedRepository;

    private SaleResponseDTO toResponse(Sale sale) {
        List<TradedResponseDTO> tradedItems = tradedRepository.findBySaleId(sale.getId())
                .stream()
                .map(traded -> new TradedResponseDTO(
                        traded.getId().getSaleId(),
                        traded.getProduct().getId(),
                        traded.getProduct().getDisplayName(),
                        traded.getFinalPrice()))
                .collect(Collectors.toList());

        return new SaleResponseDTO(
                sale.getId(),
                sale.getRuralProperty().getId(),
                sale.getRuralProperty().getName(),
                sale.getRuralProperty().getCode(),
                sale.getSaleDate(),
                sale.getTotalValue(),
                sale.getBuyerName(),
                sale.getPaymentMethod(),
                sale.getNotes(),
                tradedItems
        );
    }

    /**
     * Convert DTO to entity (registration data only)
     */
    private Sale toEntity(SaleRequestDTO dto, RuralProperty property) {
        Sale newSale = new Sale();
        newSale.setRuralProperty(property);
        newSale.setBuyerName(dto.buyerName());
        newSale.setPaymentMethod(dto.paymentMethod());
        newSale.setNotes(dto.notes());
        newSale.setSaleDate(LocalDateTime.now());
        newSale.setTotalValue(BigDecimal.ZERO);

        return newSale;
    }

    /**
     * It processes each item in the list
     * Validates it, creates the Traded link, and reduces the inventory
     */
    private BigDecimal processSaleItems(Sale sale, List<ItemRequestDTO> items) {
        BigDecimal totalAccumulated = BigDecimal.ZERO;

        for (ItemRequestDTO itemDto : items) {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemDto.productId()));

            if (product.getProductStatus() == ProductStatus.INDISPONIVEL) {
                throw new BusinessException(
                        "Product " + product.getDisplayName() + "  is already sold or unavailable.");
            }

            Traded traded = new Traded();
            TradedId tradedId = new TradedId(sale.getId(), product.getId());
            traded.setId(tradedId);
            traded.setSale(sale);
            traded.setProduct(product);
            traded.setFinalPrice(itemDto.finalPrice());
            tradedRepository.save(traded);

            product.setProductStatus(ProductStatus.INDISPONIVEL);
            productRepository.save(product);

            totalAccumulated = totalAccumulated.add(itemDto.finalPrice());
        }

        return totalAccumulated;
    }

    /**
     * Retrieves all sales
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDTO> getAllSales() {
        return saleRepository.findAllWithProperty().stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * Retrieves sales for a specific property
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDTO> getSalesByProperty(Integer propertyId) {
        if (!ruralPropertyRepository.existsById(propertyId)) {
            throw new ResourceNotFoundException("No properties were found with the provided ID.");
        }

        return saleRepository.findByRuralPropertyId(propertyId).stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the sale using the provided ID
     */
    @Transactional(readOnly = true)
    public SaleResponseDTO getSaleById(Integer id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no sale recorded with the provided ID."));

        return toResponse(sale);
    }

    /**
     * Retrieves sales made within a specific period
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDTO> getSalesByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findBySaleDateBetween(startDate, endDate).stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve sales by buyer name
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDTO> getSalesByBuyerName(String buyerName) {
        return saleRepository.findByBuyerNameContainingIgnoreCase(buyerName).stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve sales by a specific payment method
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDTO> getSalesByPaymentMethod(PaymentMethod paymentMethod) {
        return saleRepository.findByPaymentMethod(paymentMethod).stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Saves a new sale
     */
    @Transactional
    public SaleResponseDTO saveSale(SaleRequestDTO dto) {
        RuralProperty property = ruralPropertyRepository.findById(dto.propertyId())
                .orElseThrow(() -> new ResourceNotFoundException("No properties were found with the provided ID."));

        Sale sale = toEntity(dto, property);
        Sale savedSale = saleRepository.save(sale);

        BigDecimal totalCalculated = processSaleItems(savedSale, dto.items());

        savedSale.setTotalValue(totalCalculated);

        return toResponse(saleRepository.save(savedSale));
    }

    /**
     * Update existing product
     */
    @Transactional
    public SaleResponseDTO updateSale(Integer id, SaleRequestDTO dto) {
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No sales found with ID: " + id));

        if (!existingSale.getRuralProperty().getId().equals(dto.propertyId())) {
            throw new BusinessException("The rural property linked to the sale cannot be modified.");
        }

        existingSale.setBuyerName(dto.buyerName());
        existingSale.setPaymentMethod(dto.paymentMethod());
        existingSale.setNotes(dto.notes());

        return toResponse(saleRepository.save(existingSale));
    }

    /**
     * Delete a sale
     */
    @Transactional
    public void deleteSale(Integer id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cannot delete: Sale record not found with ID: " + id));

        List<Traded> tradedItems = tradedRepository.findBySaleId(id);

        for (Traded item : tradedItems) {
            Product product = item.getProduct();
            product.setProductStatus(ProductStatus.DISPONIVEL);
            productRepository.save(product);
        }

        tradedRepository.deleteAll(tradedItems);

        saleRepository.delete(sale);
    }
}
