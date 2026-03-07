package com.agribusiness360.backend.controller;

import java.time.LocalDateTime;
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
import com.agribusiness360.backend.dto.SaleRequestDTO;
import com.agribusiness360.backend.dto.SaleResponseDTO;
import com.agribusiness360.backend.model.PaymentMethod;
import com.agribusiness360.backend.service.SaleService;

@RestController
@RequestMapping("/api/sales")
/** SECURITY CONFIGURATION (CORS):
 *  - Currently configured to accept requests from the Vue/Vite Frontend 
 *  (localhost:5173).
 *  - IMPORTANT: For production environments, 
 *  replace "http://localhost:5173" with the official URL where your 
 *  Frontend is hosted.
 */
@CrossOrigin(origins = "http://localhost:5173")
public class SaleController {

    @Autowired
    private SaleService saleService;

    /**
     *  Search all sales
     */
    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    /**
     *  Search sales by property ID
     */
    @GetMapping("/property/{id}")
    public ResponseEntity<List<SaleResponseDTO>> getSalesByPropertyId(@PathVariable Integer id) {
        return ResponseEntity.ok(saleService.getSalesByProperty(id));
    }

    /**
     *  Search for a sale by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> getSaleById(@PathVariable Integer id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    /**
     *  Search sales within a specific period
     */
    @GetMapping("/period")
    public ResponseEntity<List<SaleResponseDTO>> getSalesByPeriod(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(saleService.getSalesByPeriod(startDate, endDate));
    }

    /**
     *  Search sales by buyer name
     */
    @GetMapping("/buyer-name")
    public ResponseEntity<List<SaleResponseDTO>> getSalesByBuyerName(@RequestParam String buyerName) {
        return ResponseEntity.ok(saleService.getSalesByBuyerName(buyerName));
    }

    /**
     *  Search sales by payment method
    */
   @GetMapping("/payment-method")
   public ResponseEntity<List<SaleResponseDTO>> getSalesByPaymentMethod(@RequestParam PaymentMethod paymentMethod) {
        return ResponseEntity.ok(saleService.getSalesByPaymentMethod(paymentMethod));
   }

   /**
    *   Save new sale
    */
   @PostMapping
   public ResponseEntity<SaleResponseDTO> saveSale(@RequestBody SaleRequestDTO dto) {
        SaleResponseDTO newSale = saleService.saveSale(dto);

        return new ResponseEntity<>(newSale, HttpStatus.CREATED);
   }

   /**
    *   Update sale
    */
   @PutMapping("{id}")
   public ResponseEntity<SaleResponseDTO> updateSale(@PathVariable Integer id, @RequestBody SaleRequestDTO dto) {
        SaleResponseDTO updatedSale = saleService.updateSale(id, dto);

        return ResponseEntity.ok(updatedSale);
   }

   /**
    *   Delete sale
    */
   @DeleteMapping("{id}")
   public ResponseEntity<Void> deleteSale(@PathVariable Integer id) {
        saleService.deleteSale(id);

        return ResponseEntity.noContent().build();
   }
}
