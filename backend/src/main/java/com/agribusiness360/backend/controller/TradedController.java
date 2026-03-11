package com.agribusiness360.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.agribusiness360.backend.model.Traded;
import com.agribusiness360.backend.service.TradedService;

@RestController
@RequestMapping("/api/traded")
/** SECURITY CONFIGURATION (CORS):
 *  - Currently configured to accept requests from the Vue/Vite Frontend 
 *  (localhost:5173).
 *  - IMPORTANT: For production environments, 
 *  replace "http://localhost:5173" with the official URL where your 
 *  Frontend is hosted.
 */
@CrossOrigin(origins = "http://localhost:5173")
public class TradedController {

    @Autowired
    private TradedService tradedService;

    /**
     * Search all commercial records
     */
    @GetMapping
    public ResponseEntity<List<Traded>> getAllTradedItems() {
        return ResponseEntity.ok(tradedService.getAllTradedItems());
    }

    /**
     * Search all products included in a specific sale
     */
    @GetMapping("/sale/{id}")
    public ResponseEntity<List<Traded>> getItemsBySale(@PathVariable Integer id) {
        return ResponseEntity.ok(tradedService.getItemsBySale(id));
    }

    /**
     * Find a specific trade record by Sale and Product
     */
    @GetMapping("/sale/{idSale}/product/{idProduct}")
    public ResponseEntity<Traded> getTradedItem(@PathVariable Integer idSale, @PathVariable Integer idProduct) {
        return ResponseEntity.ok(tradedService.getTradedItem(idSale, idProduct));
    }
}
