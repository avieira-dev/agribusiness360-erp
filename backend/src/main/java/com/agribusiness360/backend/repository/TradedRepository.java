package com.agribusiness360.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agribusiness360.backend.model.Traded;
import com.agribusiness360.backend.model.TradedId;

@Repository
public interface TradedRepository extends JpaRepository<Traded, TradedId> {

    /**
     *  List all items included in a specific sale
     */
    List<Traded> findBySaleId(Integer saleId);

    /**
     * List all trade records for a specific product
     */
    List<Traded> findByProductId(Integer productId);
}
