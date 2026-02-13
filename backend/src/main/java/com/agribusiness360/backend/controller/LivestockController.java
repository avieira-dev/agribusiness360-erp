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
import com.agribusiness360.backend.model.AnimalSex;
import com.agribusiness360.backend.model.AnimalType;
import com.agribusiness360.backend.model.HealthStatus;
import com.agribusiness360.backend.model.Livestock;
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
    public ResponseEntity<List<Livestock>> getAllAnimals() {
        List<Livestock> animals = livestockService.getAllAnimals();

        return ResponseEntity.ok(animals);
    }

    /**
     *  Search all animals from a specific property
     */
    @GetMapping("/property/{id}")
    public ResponseEntity<List<Livestock>> getAllAnimalsByProperty(@PathVariable Integer id) {
        List<Livestock> animals = livestockService.getAllAnimalsByProperty(id);

        return ResponseEntity.ok(animals);
    }

    /**
     *  Search all animals of a specific type from a specific property
     */
    @GetMapping("/property/{id}/type")
    public ResponseEntity<List<Livestock>> getAnimalsOfSpecificTypeFromSpecificProperty(@PathVariable Integer id, @RequestParam AnimalType animalType) {
        List<Livestock> animals = livestockService.getAllAnimalsByPropertyIdAndAnimalType(id, animalType);

        return ResponseEntity.ok(animals);
    }

    /**
     *  Search animals by sex
     */
    @GetMapping("/animal-sex")
    public ResponseEntity<List<Livestock>> getAnimalsBySex(@RequestParam AnimalSex animalSex) {
        List<Livestock> animals = livestockService.getAnimalsBySpecificSex(animalSex);

        return ResponseEntity.ok(animals);
    }

    /**
     *  Search animals by health status
     */
    @GetMapping("/health-status")
    public ResponseEntity<List<Livestock>> getAnimalsByHealthStatus(@RequestParam HealthStatus healthStatus) {
        List<Livestock> animals = livestockService.getAllAnimalsByHealthStatus(healthStatus);

        return ResponseEntity.ok(animals);
    }

    /**
     *  Search animals by health status from a specific property
     */
    @GetMapping("/property/{id}/health-status")
    public ResponseEntity<List<Livestock>> getAnimalsByHealthStatusFromSpecificProperty(@PathVariable Integer id, @RequestParam HealthStatus healthStatus) {
        List<Livestock> animals = livestockService.getAllAnimalsByPropertyIdAndHealthStatus(id, healthStatus);

        return ResponseEntity.ok(animals);
    }

    /**
     *  Search for an animal by identification code
     */
    @GetMapping("/animal-code")
    public ResponseEntity<Livestock> getAnimalByCode(@RequestParam String code) {
        Livestock animal = livestockService.getAnimalByUniqueCode(code);

        return ResponseEntity.ok(animal);
    }
    
    /**
     *  Search for an animal by traceability code
     */
    @GetMapping("/animal-traceability")
    public ResponseEntity<Livestock> getAnimalByTraceability(@RequestParam String traceability) {
        Livestock animal = livestockService.getAnimalByTraceability(traceability);

        return ResponseEntity.ok(animal);
    }

    /**
     *  Save a new animal
     */
    @PostMapping
    public ResponseEntity<Livestock> saveAnimal(@RequestBody Livestock animal) {
        Livestock newAnimal = livestockService.saveAnimal(animal);

        return new ResponseEntity<>(newAnimal, HttpStatus.CREATED);
    }

    /**
     *  Update animal
     */
    @PutMapping("/{id}")
    public ResponseEntity<Livestock> updateAnimal(@PathVariable Integer id, @RequestBody Livestock animalDetails) {
        Livestock updated = livestockService.updateAnimal(id, animalDetails);

        return ResponseEntity.ok(updated);
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

