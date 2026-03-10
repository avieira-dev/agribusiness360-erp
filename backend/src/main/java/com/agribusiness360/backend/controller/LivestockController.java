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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.agribusiness360.backend.dto.LivestockRequestDTO;
import com.agribusiness360.backend.dto.LivestockResponseDTO;
import com.agribusiness360.backend.model.AnimalSex;
import com.agribusiness360.backend.model.AnimalType;
import com.agribusiness360.backend.model.HealthStatus;
import com.agribusiness360.backend.service.LivestockService;

@RestController
@RequestMapping("/api/livestock")
/** SECURITY CONFIGURATION (CORS):
 *  - Currently configured to accept requests from the Vue/Vite Frontend 
 *  (localhost:5173).
 *  - IMPORTANT: For production environments, 
 *  replace "http://localhost:5173" with the official URL where your 
 *  Frontend is hosted.
 */
@CrossOrigin(origins = "http://localhost:5173")
public class LivestockController {

    @Autowired
    private LivestockService livestockService;

    /**
     *  Search all animals
     */
    @GetMapping
    public ResponseEntity<List<LivestockResponseDTO>> getAllAnimals() {
        return ResponseEntity.ok(livestockService.getAllAnimals());
    }

    /**
     *  Search all animals from a specific property
     */
    @GetMapping("/property/{id}")
    public ResponseEntity<List<LivestockResponseDTO>> getAllAnimalsByProperty(@PathVariable Integer id) {
        return ResponseEntity.ok(livestockService.getAllAnimalsByProperty(id));
    }

    /**
     *  Search all animals of a specific type from a specific property
     */
    @GetMapping("/property/{id}/type")
    public ResponseEntity<List<LivestockResponseDTO>> getAnimalsOfSpecificTypeFromSpecificProperty(@PathVariable Integer id, @RequestParam AnimalType animalType) {
        return ResponseEntity.ok(livestockService.getAllAnimalsByPropertyIdAndAnimalType(id, animalType));
    }

    /**
     *  Search animals by sex
     */
    @GetMapping("/animal-sex")
    public ResponseEntity<List<LivestockResponseDTO>> getAnimalsBySex(@RequestParam AnimalSex animalSex) {
        return ResponseEntity.ok(livestockService.getAnimalsBySpecificSex(animalSex));
    }

    /**
     *  Search animals by health status
     */
    @GetMapping("/health-status")
    public ResponseEntity<List<LivestockResponseDTO>> getAnimalsByHealthStatus(@RequestParam HealthStatus healthStatus) {
        return ResponseEntity.ok(livestockService.getAllAnimalsByHealthStatus(healthStatus));
    }

    /**
     *  Search animals by health status from a specific property
     */
    @GetMapping("/property/{id}/health-status")
    public ResponseEntity<List<LivestockResponseDTO>> getAnimalsByHealthStatusFromSpecificProperty(@PathVariable Integer id, @RequestParam HealthStatus healthStatus) {
        return ResponseEntity.ok(livestockService.getAllAnimalsByPropertyIdAndHealthStatus(id, healthStatus));
    }

    /**
     *  Search for an animal by identification code
     */
    @GetMapping("/animal-code")
    public ResponseEntity<LivestockResponseDTO> getAnimalByCode(@RequestParam String code) {
        return ResponseEntity.ok(livestockService.getAnimalByUniqueCode(code));
    }
    
    /**
     *  Search for an animal by traceability code
     */
    @GetMapping("/animal-traceability")
    public ResponseEntity<LivestockResponseDTO> getAnimalByTraceability(@RequestParam String traceability) {
        return ResponseEntity.ok(livestockService.getAnimalByTraceability(traceability));
    }

    /**
     *  Save a new animal
     */
    @PostMapping
    public ResponseEntity<LivestockResponseDTO> saveAnimal(@RequestBody LivestockRequestDTO dto) {
        LivestockResponseDTO newAnimal = livestockService.saveAnimal(dto);

        return new ResponseEntity<>(newAnimal, HttpStatus.CREATED);
    }

    /**
     *  Update animal
     */
    @PutMapping("/{id}")
    public ResponseEntity<LivestockResponseDTO> updateAnimal(@PathVariable Integer id, @RequestBody LivestockRequestDTO dto) {
        LivestockResponseDTO updatedAnimal = livestockService.updateAnimal(id, dto);

        return ResponseEntity.ok(updatedAnimal);
    }

    /**
     *  Delete animal
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Integer id) {
        livestockService.deleteAnimal(id);

        return ResponseEntity.noContent().build();
    }
}

