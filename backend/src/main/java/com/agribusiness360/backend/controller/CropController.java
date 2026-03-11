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
import com.agribusiness360.backend.dto.CropRequestDTO;
import com.agribusiness360.backend.dto.CropResponseDTO;
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
    public ResponseEntity<List<CropResponseDTO>> getAllCrops() {
        return ResponseEntity.ok(cropService.getAllCrops());
    }

    /**
     *  Search crops by plot
     */
    @GetMapping("/plot/{id}")
    public ResponseEntity<List<CropResponseDTO>> getCropsByPlot(@PathVariable Integer id) {
        return ResponseEntity.ok(cropService.getCropsByPlotId(id));
    }

    /**
     *  Search crops by name
     */
    @GetMapping("/search-name")
    public ResponseEntity<List<CropResponseDTO>> getCropByName(@RequestParam String name) {
        return ResponseEntity.ok(cropService.getCropByName(name));
    }

    /**
     *  Search crops by crop type
     */
    @GetMapping("/search-culture")
    public ResponseEntity<List<CropResponseDTO>> getCropsByCulture(@RequestParam CultureType cultureType) {
        return ResponseEntity.ok(cropService.getCropsByCultureType(cultureType));
    }

    /**
     *  Search crops by custom crop type
     */
    @GetMapping("/specific-culture")
    public ResponseEntity<List<CropResponseDTO>> getCropsBySpecificCulture(@RequestParam String specificCulture) {
        return ResponseEntity.ok(cropService.getCropsSpecificCulture(specificCulture));
    }

    /**
     *  Search crops by status
     */
    @GetMapping("/search-status")
    public ResponseEntity<List<CropResponseDTO>> getCropsByStatus(@RequestParam CropStatus cropStatus) {
        return ResponseEntity.ok(cropService.getCropsByStatus(cropStatus));
    }

    /**
     *  Search crops within a specific period
     */
    @GetMapping("/period")
    public ResponseEntity<List<CropResponseDTO>> getCropsByPeriod(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(cropService.getCropsByPlantDateBetween(startDate, endDate));
    }

    /**
     *  Save new crop
     */
    @PostMapping
    public ResponseEntity<CropResponseDTO> saveCrop(@RequestBody CropRequestDTO dto) {
        CropResponseDTO newCrop = cropService.saveCrop(dto);

        return new ResponseEntity<>(newCrop, HttpStatus.CREATED);
    }

    /**
     *  Update crop
     */
    @PutMapping("/{id}")
    public ResponseEntity<CropResponseDTO> updateCrop(@PathVariable Integer id, @RequestBody CropRequestDTO dto) {
        CropResponseDTO updatedCrop = cropService.updateCrop(id, dto);

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
