package com.agribusiness360.backend.service;

import com.agribusiness360.backend.model.PaymentMethod;
import com.agribusiness360.backend.model.Sale;
import com.agribusiness360.backend.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    /**
     *  Retrieves all sales
     */
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    /**
     *  Retrieves sales for a specific property
     */
    public List<Sale> getSalesByProperty(Integer propertyId) {
        return saleRepository.findByRuralPropertyId(propertyId);
    }

    /*
     *  Retrieves the sale using the provided ID
     */
    public Sale getSaleById(Integer id) {
        return saleRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("There is no sale recorded with the provided ID."));
    }

    /**
     *  Retrieves sales made within a specific period
     */
    public List<Sale> getSalesByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findBySaleDateBetween(startDate, endDate);
    }

    /**
     *  Retrieve sales by buyer name
     */
    public List<Sale> getSalesByBuyerName(String name) {
        return saleRepository.findByBuyerNameContainingIgnoreCase(name);
    }

    /**
     *  Retrieve sales by a specific payment method
     */
    public List<Sale> getSalesByPaymentMethod(PaymentMethod paymentMethod) {
        return saleRepository.findByPaymentMethod(paymentMethod);
    }

    /**
     *  Saves a new sale
     */
    @Transactional
    public Sale saveSale(Sale sale) {
        if(sale.getTotalValue() != null && sale.getTotalValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("The total value of a sale cannot be negative.");
        }

        if(sale.getSaleDate() == null) {
            sale.setSaleDate(LocalDateTime.now());
        }

        return saleRepository.save(sale);
    }

    /**
     *  Update existing product
     */
    @Transactional
    public Sale updateSale(Integer id, Sale details) {
        Sale existingSale = saleRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("There is no sale registered with the provided ID."));

        existingSale.setBuyerName(details.getBuyerName());
        existingSale.setPaymentMethod(details.getPaymentMethod());
        existingSale.setTotalValue(details.getTotalValue());
        existingSale.setNotes(details.getNotes());

        return saleRepository.save(existingSale);
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
