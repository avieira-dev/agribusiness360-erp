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
import com.agribusiness360.backend.model.Plot;
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
    public ResponseEntity<List<Plot>> getAllPlots() {
        List<Plot> plots = plotService.getAllPlots();

        return ResponseEntity.ok(plots);
    }

    /**
     *  Search for a plot by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Plot> getPlotById(@PathVariable Integer id) {
        Plot plot = plotService.getPlotById(id);

        return ResponseEntity.ok(plot);
    }

    /**
     *  Search plots from a specific property
     */
    @GetMapping("/property/{id}")
    public ResponseEntity<List<Plot>> getPlotsByPropertyId(@PathVariable Integer id) {
        List<Plot> plots = plotService.getPlotsByProperty(id);

        return ResponseEntity.ok(plots);
    }

    /**
     *  Search plots by name
     */
    @GetMapping("/search-name")
    public ResponseEntity<List<Plot>> getPlotsByName(@RequestParam String name) {
        List<Plot> plots = plotService.getPlotsByName(name);

        return ResponseEntity.ok(plots);
    }

    /**
     *  Search for a plot by code
     */
    @GetMapping("/search-code")
    public ResponseEntity<Plot> getPlotByCode(@RequestParam String code) {
        return ResponseEntity.ok(plotService.getPlotByUniqueCode(code));
    }

    /**
     *  Search plots by soil
     */
    @GetMapping("/search-soil")
    public ResponseEntity<List<Plot>> getPlotsBySoil(@RequestParam SoilType soilType) {
        List<Plot> plots = plotService.getPlotsBySoilType(soilType);

        return ResponseEntity.ok(plots);
    }

    /**
     *  Save a new plot
     */
    @PostMapping
    public ResponseEntity<Plot> savePlot(@RequestBody Plot plot) {
        Plot newPlot = plotService.savePlot(plot);

        return new ResponseEntity<>(newPlot, HttpStatus.CREATED);
    }

    /**
     *  Update an existing plot
     */
    @PutMapping("/{id}")
    public ResponseEntity<Plot> updatePlot(@PathVariable Integer id, @RequestBody Plot plotDetails) {
        Plot updatedPlot = plotService.updatePlot(id, plotDetails);

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
