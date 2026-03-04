package com.agribusiness360.backend.controller;

import java.util.List;
import com.agribusiness360.backend.dto.PlotRequestDTO;
import com.agribusiness360.backend.dto.PlotResponseDTO;
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
import com.agribusiness360.backend.model.SoilType;
import com.agribusiness360.backend.service.PlotService;

@RestController
@RequestMapping("/api/plots")
/** SECURITY CONFIGURATION (CORS):
 *  - Currently configured to accept requests from the Vue/Vite Frontend 
 *  (localhost:5173).
 *  - IMPORTANT: For production environments, 
 *  replace "http://localhost:5173" with the official URL where your 
 *  Frontend is hosted.
 */
@CrossOrigin(origins = "http://localhost:5173")
public class PlotController {

    @Autowired
    private PlotService plotService;

    /**
     *  Search all plots
     */
    @GetMapping
    public ResponseEntity<List<PlotResponseDTO>> getAllPlots() {
        return ResponseEntity.ok(plotService.getAllPlots());
    }

    /**
     *  Search for a plot by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlotResponseDTO> getPlotById(@PathVariable Integer id) {
        return ResponseEntity.ok(plotService.getPlotById(id));
    }

    /**
     *  Search plots from a specific property
     */
    @GetMapping("/property/{id}")
    public ResponseEntity<List<PlotResponseDTO>> getPlotsByPropertyId(@PathVariable Integer id) {
        return ResponseEntity.ok(plotService.getPlotsByProperty(id));
    }

    /**
     *  Search plots by name
     */
    @GetMapping("/search-name")
    public ResponseEntity<List<PlotResponseDTO>> getPlotsByName(@RequestParam String name) {
        return ResponseEntity.ok(plotService.getPlotsByName(name));
    }

    /**
     *  Search for a plot by code
     */
    @GetMapping("/search-code")
    public ResponseEntity<PlotResponseDTO> getPlotByCode(@RequestParam String code) {
        return ResponseEntity.ok(plotService.getPlotByUniqueCode(code));
    }

    /**
     *  Search plots by soil
     */
    @GetMapping("/search-soil")
    public ResponseEntity<List<PlotResponseDTO>> getPlotsBySoil(@RequestParam SoilType soilType) {
        return ResponseEntity.ok(plotService.getPlotsBySoilType(soilType));
    }

    /**
     *  Save a new plot
     */
    @PostMapping
    public ResponseEntity<PlotResponseDTO> savePlot(@RequestBody PlotRequestDTO dto) {
        PlotResponseDTO newPlot = plotService.savePlot(dto);

        return new ResponseEntity<>(newPlot, HttpStatus.CREATED);
    }

    /**
     *  Update an existing plot
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlotResponseDTO> updatePlot(@PathVariable Integer id, @RequestBody PlotRequestDTO  dto) {
        PlotResponseDTO updatedPlot = plotService.updatePlot(id, dto);

        return ResponseEntity.ok(updatedPlot);
    }

    /**
     *  Delete plot
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlot(@PathVariable Integer id) {
        plotService.deletePlot(id);

        return ResponseEntity.noContent().build();
    }
}
