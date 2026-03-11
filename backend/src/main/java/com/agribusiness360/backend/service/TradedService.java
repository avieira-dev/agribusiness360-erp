package com.agribusiness360.backend.service;

import java.util.List;
import com.agribusiness360.backend.exception.ResourceNotFoundException;
import com.agribusiness360.backend.model.Traded;
import com.agribusiness360.backend.model.TradedId;
import com.agribusiness360.backend.repository.TradedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TradedService {

    @Autowired
    private TradedRepository tradedRepository;

     /**
      * List all trade records in the system
      */
    @Transactional(readOnly = true)
    public List<Traded> getAllTradedItems() {
        return tradedRepository.findAll();
    }

    /**
     * List all products included in a specific sale
     */
    @Transactional(readOnly = true)
    public List<Traded> getItemsBySale(Integer saleId) {
        return tradedRepository.findBySaleId(saleId);
    }

    /**
     * Find a specific trade record by its composite ID
     */
    @Transactional(readOnly = true)
    public Traded getTradedItem(Integer saleId, Integer productId) {
        TradedId id = new TradedId(saleId, productId);
        return tradedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trade record not found for this sale and product."));
    }
}
