package com.agribusiness360.backend.controller;

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
     *  Search all commercial records
     */
    @GetMapping
    public ResponseEntity<List<Traded>> getAllTradedItems() {
        List<Traded> tradedItems = tradedService.getAllTradedItems();

        return ResponseEntity.ok(tradedItems);
    }

    /**
     *  Search all products included in a sale
     */
    @GetMapping("/sale/{id}")
    public ResponseEntity<List<Traded>> getItemsBySale(@PathVariable Integer id) {
        List<Traded> tradedItems = tradedService.getItemsBySale(id);

        return ResponseEntity.ok(tradedItems);
    }

    /**
     *  Save a new traded
     */
    @PostMapping
    public ResponseEntity<Traded> saveTraded(@RequestBody Traded traded) {
        Traded newTraded = tradedService.saveTraded(traded);

        return new ResponseEntity<>(newTraded, HttpStatus.CREATED);
    }

    /**
     *  Updates the price of an item already linked to a sale
     */
    @PutMapping("/sale/{idSale}/product/{idProduct}")
    public ResponseEntity<Traded> updateTraded(@PathVariable Integer idSale, @PathVariable Integer idProduct, @RequestBody Traded tradedDetails) {
        Traded updatedTraded = tradedService.updateTradedPrice(idSale, idProduct, tradedDetails.getFinalPrice());

        return ResponseEntity.ok(updatedTraded);
    }

    /**
     *  Delete traded
     */
    @DeleteMapping("/sale/{idSale}/product/{idProduct}")
    public ResponseEntity<Void> deleteTraded(@PathVariable Integer idSale, @PathVariable Integer idProduct) {
        tradedService.deleteTraded(idSale, idProduct);

        return ResponseEntity.noContent().build();
    }
}
