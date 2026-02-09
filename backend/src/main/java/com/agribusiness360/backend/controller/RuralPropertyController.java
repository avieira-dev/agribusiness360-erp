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
import com.agribusiness360.backend.model.RuralProperty;
import com.agribusiness360.backend.service.RuralPropertyService;

@RestController
@RequestMapping("/api/rural-properties")
/** SECURITY CONFIGURATION (CORS):
 *  - Currently configured to accept requests from the Vue/Vite Frontend 
 *  (localhost:5173).
 *  - IMPORTANT: For production environments, 
 *  replace "http://localhost:5173" with the official URL where your 
 *  Frontend is hosted.
 */
@CrossOrigin(origins = "http://localhost:5173")
public class RuralPropertyController {

    @Autowired
    private RuralPropertyService ruralPropertyService;

    /**
     *  Search for all properties
     */
    @GetMapping
    public ResponseEntity<List<RuralProperty>> getAllProperties() {
        List<RuralProperty> properties = ruralPropertyService.getAllRuralProperties();

        return ResponseEntity.ok(properties);
    }

    /**
     *  Search for a property by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RuralProperty> getPropertyById(@PathVariable Integer id) {
        RuralProperty property = ruralPropertyService.getRuralPropertyId(id);

        return ResponseEntity.ok(property);
    }

    /**
     *  Search for a property by name
     */
    @GetMapping("/search-name")
    public ResponseEntity<RuralProperty> getPropertyByName(@RequestParam String name) {
        return ResponseEntity.ok(ruralPropertyService.getByName(name));
    }

    /**
     *  Search for a property by code
     */
    @GetMapping("/search-code")
    public ResponseEntity<RuralProperty> getPropertyByCode(@RequestParam String code) {
        return ResponseEntity.ok(ruralPropertyService.getByCode(code));
    }

    /**
     *  Create a new property
     */
    @PostMapping
    public ResponseEntity<RuralProperty> createProperty(@RequestBody RuralProperty property) {
        RuralProperty ruralProperty = ruralPropertyService.saveRuralProperty(property);

        return new ResponseEntity<>(ruralProperty, HttpStatus.CREATED);
    }

    /**
     *  Update an existing property
     */
    @PutMapping("/{id}")
    public ResponseEntity<RuralProperty> updateProperty(@PathVariable Integer id, @RequestBody RuralProperty propertyDetails) {
        return ResponseEntity.ok(ruralPropertyService.updateRuralProperty(id, propertyDetails));
    }

    /**
     *  Delete property
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Integer id) {
        ruralPropertyService.deleteRuralProperty(id);

        return ResponseEntity.noContent().build();
    }
}
