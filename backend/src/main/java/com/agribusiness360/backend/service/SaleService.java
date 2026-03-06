package com.agribusiness360.backend.service;

import com.agribusiness360.backend.dto.SaleRequestDTO;
import com.agribusiness360.backend.dto.SaleResponseDTO;
import com.agribusiness360.backend.exception.BusinessException;
import com.agribusiness360.backend.exception.ResourceNotFoundException;
import com.agribusiness360.backend.model.PaymentMethod;
import com.agribusiness360.backend.model.RuralProperty;
import com.agribusiness360.backend.model.Sale;
import com.agribusiness360.backend.repository.RuralPropertyRepository;
import com.agribusiness360.backend.repository.SaleRepository;
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

    /**
     *  Convert entity to DTO
     */
    private SaleResponseDTO toResponse(Sale sale) {
        return new SaleResponseDTO(
                sale.getId(),
                sale.getRuralProperty().getId(),
                sale.getRuralProperty().getName(),
                sale.getRuralProperty().getCode(),
                sale.getSaleDate(),
                sale.getTotalValue(),
                sale.getBuyerName(),
                sale.getPaymentMethod(),
                sale.getNotes()
        );
    }

    /**
     *  Convert DTO to entity
     */
    private Sale toEntity(SaleRequestDTO dto) {
        Sale newSale = new Sale();

        RuralProperty property = ruralPropertyRepository.findById(dto.propertyId())
                .orElseThrow(() -> new ResourceNotFoundException("No properties were found with the provided ID."));

        if(dto.totalValue() != null && dto.totalValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("The total value of a sale cannot be negative.");
        }

        LocalDateTime dateToSave = (dto.saleDate() == null) ? LocalDateTime.now() : dto.saleDate();

        newSale.setRuralProperty(property);
        newSale.setSaleDate(dateToSave);
        newSale.setTotalValue(dto.totalValue());
        newSale.setBuyerName(dto.buyerName());
        newSale.setPaymentMethod(dto.paymentMethod());
        newSale.setNotes(dto.notes());

        return newSale;
    }

    /**
     *  Retrieves all sales
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDTO> getAllSales() {
        return saleRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves sales for a specific property
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDTO> getSalesByProperty(Integer propertyId) {
        if(!ruralPropertyRepository.existsById(propertyId)) {
            throw new ResourceNotFoundException("No properties were found with the provided ID.");
        }

        return saleRepository.findByRuralPropertyId(propertyId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves the sale using the provided ID
     */
    @Transactional(readOnly = true)
    public SaleResponseDTO getSaleById(Integer id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no sale recorded with the provided ID."));

        return toResponse(sale);
    }

    /**
     *  Retrieves sales made within a specific period
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDTO> getSalesByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findBySaleDateBetween(startDate, endDate).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieve sales by buyer name
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDTO> getSalesByBuyerName(String buyerName) {
        return saleRepository.findByBuyerNameContainingIgnoreCase(buyerName).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieve sales by a specific payment method
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDTO> getSalesByPaymentMethod(PaymentMethod paymentMethod) {
        return saleRepository.findByPaymentMethod(paymentMethod).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Saves a new sale
     */
    @Transactional
    public SaleResponseDTO saveSale(SaleRequestDTO dto) {
        Sale sale = toEntity(dto);

        return toResponse(saleRepository.save(sale));
    }

    /**
     *  Update existing product
     */
    @Transactional
    public SaleResponseDTO updateSale(Integer id, SaleRequestDTO dto) {
        if(!saleRepository.existsById(id)) {
            throw new ResourceNotFoundException("No sales found with that ID.");
        }

        Sale sale = toEntity(dto);

        sale.setId(id);

        return toResponse(saleRepository.save(sale));
    }

    /**
     *  Delete a sale
     */
    @Transactional
    public void deleteSale(Integer id) {
        if(!saleRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Sale record not found.");
        }

        saleRepository.deleteById(id);
    }
}
