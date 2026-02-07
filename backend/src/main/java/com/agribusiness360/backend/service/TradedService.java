package com.agribusiness360.backend.service;

import java.math.BigDecimal;
import java.util.List;
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
     *  Retrieves all commercial records
     */
    public List<Traded> getAllTradedItems() {
        return tradedRepository.findAll();
    }

    /**
     *  List all products included in a specific sale
     */
    public List<Traded> getItemsBySale(Integer saleId) {
        return tradedRepository.findBySaleId(saleId);
    }

    /**
     *  Save a new traded
     */
    @Transactional
    public Traded saveTraded(Traded traded) {
        if(traded.getFinalPrice() != null && traded.getFinalPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Final price cannot be negative.");
        }

        return tradedRepository.save(traded);
    }

    /**
     *  Updates the price of an item already linked to a sale
     */
    @Transactional
    public Traded updateTradedPrice(Integer saleId, Integer productId, BigDecimal newPrice) {
        TradedId id = new TradedId(saleId, productId);
        Traded traded = tradedRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Trade record not found for this sale and product."));

        if(newPrice != null && newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("The new final price cannot be negative.");
        }

        traded.setFinalPrice(newPrice);

        return tradedRepository.save(traded);
    }

    /**
     *  Delete traded
     */
    @Transactional
    public void deleteTraded(Integer saleId, Integer productId) {
        TradedId id = new TradedId(saleId, productId);

        if(!tradedRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Trade record not found.");
        }

        tradedRepository.deleteById(id);
    }
}
