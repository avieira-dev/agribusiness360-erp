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
import com.agribusiness360.backend.model.PaymentMethod;
import com.agribusiness360.backend.model.Sale;
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
    public ResponseEntity<List<Sale>> getAllSales() {
        List<Sale> sales = saleService.getAllSales();

        return ResponseEntity.ok(sales);
    }

    /**
     *  Search sales by property ID
     */
    @GetMapping("/property/{id}")
    public ResponseEntity<List<Sale>> getSalesByPropertyId(@PathVariable Integer id) {
        List<Sale> sales = saleService.getSalesByProperty(id);

        return ResponseEntity.ok(sales);
    }

    /**
     *  Search for a sale by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Integer id) {
        Sale sale = saleService.getSaleById(id);

        return ResponseEntity.ok(sale);
    }

    /**
     *  Search sales within a specific period
     */
    @GetMapping("/period")
    public ResponseEntity<List<Sale>> getSalesByPeriod(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        List<Sale> sales = saleService.getSalesByPeriod(startDate, endDate);

        return ResponseEntity.ok(sales);
    }

    /**
     *  Search sales by buyer name
     */
    @GetMapping("/buyer-name")
    public ResponseEntity<List<Sale>> getSalesByBuyerName(@RequestParam String buyerName) {
        List<Sale> sales = saleService.getSalesByBuyerName(buyerName);

        return ResponseEntity.ok(sales);
    }

    /**
     *  Search sales by payment method
    */
   @GetMapping("/payment-method")
   public ResponseEntity<List<Sale>> getSalesByPaymentMethod(@RequestParam PaymentMethod paymentMethod) {
        List<Sale> sales = saleService.getSalesByPaymentMethod(paymentMethod);

        return ResponseEntity.ok(sales);
   }

   /**
    *   Save new sale
    */
   @PostMapping
   public ResponseEntity<Sale> saveSale(@RequestBody Sale sale) {
        Sale newSale = saleService.saveSale(sale);

        return new ResponseEntity<>(newSale, HttpStatus.CREATED);
   }

   /**
    *   Update sale
    */
   @PutMapping("{id}")
   public ResponseEntity<Sale> updateSale(@PathVariable Integer id, @RequestBody Sale saleDetails) {
        Sale updatedSale = saleService.updateSale(id, saleDetails);

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
