package com.agribusiness360.backend.controller;

import java.time.LocalDate;
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
import com.agribusiness360.backend.model.Crop;
import com.agribusiness360.backend.model.CropStatus;
import com.agribusiness360.backend.model.CultureType;
import com.agribusiness360.backend.service.CropService;

@RestController
@RequestMapping("/api/crops")
/** SECURITY CONFIGURATION (CORS):
 *  - Currently configured to accept requests from the Vue/Vite Frontend
 *  (localhost:5173).
 *  - IMPORTANT: For production environments,
 *  replace "http://localhost:5173" with the official URL where your
 *  Frontend is hosted.
 */
@CrossOrigin(origins = "http://localhost:5173")
public class CropController {

    @Autowired
    private CropService cropService;

    /**
     *  Search all crops
     */
    @GetMapping
    public ResponseEntity<List<Crop>> getAllCrops() {
        List<Crop> crops = cropService.getAllCrops();

        return ResponseEntity.ok(crops);
    }

    /**
     *  Search crops by plot
     */
    @GetMapping("/plot/{id}")
    public ResponseEntity<List<Crop>> getCropsByPlot(@PathVariable Integer id) {
        List<Crop> crops = cropService.getCropsByPlotId(id);

        return ResponseEntity.ok(crops);
    }

    /**
     *  Search crops by name
     */
    @GetMapping("/search-name")
    public ResponseEntity<List<Crop>> getCropByName(@RequestParam String name) {
        List<Crop> crops = cropService.getCropByName(name);

        return ResponseEntity.ok(crops);
    }

    /**
     *  Search crops by crop type
     */
    @GetMapping("/search-culture")
    public ResponseEntity<List<Crop>> getCropsByCulture(@RequestParam CultureType cultureType) {
        List<Crop> crops = cropService.getCropsByCultureType(cultureType);

        return ResponseEntity.ok(crops);
    }

    /**
     *  Search crops by custom crop type
     */
    @GetMapping("/specific-culture")
    public ResponseEntity<List<Crop>> getCropsBySpecificCulture(@RequestParam String specificCulture) {
        List<Crop> crops = cropService.getCropsSpecificCulture(specificCulture);

        return ResponseEntity.ok(crops);
    }

    /**
     *  Search crops by status
     */
    @GetMapping("/search-status")
    public ResponseEntity<List<Crop>> getCropsByStatus(@RequestParam CropStatus cropStatus) {
        List<Crop> crops = cropService.getCropsByStatus(cropStatus);

        return ResponseEntity.ok(crops);
    }

    /**
     *  Search crops within a specific period
     */
    @GetMapping("/period")
    public ResponseEntity<List<Crop>> getCropsByPeriod(@RequestParam LocalDate starDate, @RequestParam LocalDate endDate) {
        List<Crop> crops = cropService.getCropsByPlantDateBetween(starDate, endDate);

        return ResponseEntity.ok(crops);
    }

    /**
     *  Save new crop
     */
    @PostMapping
    public ResponseEntity<Crop> saveCrop(@RequestBody Crop crop) {
        Crop newCrop = cropService.saveCrop(crop);

        return new ResponseEntity<>(newCrop, HttpStatus.CREATED);
    }

    /**
     *  Update crop
     */
    @PutMapping("/{id}")
    public ResponseEntity<Crop> updateCrop(@PathVariable Integer id, @RequestBody Crop cropDetails) {
        Crop updatedCrop = cropService.updateCrop(id, cropDetails);

        return ResponseEntity.ok(updatedCrop);
    }

    /**
     *  Delete crop
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCrop(@PathVariable Integer id) {
        cropService.deleteCrop(id);

        return ResponseEntity.noContent().build();
    }
}
