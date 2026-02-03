package com.agribusiness360.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.agribusiness360.backend.model.Plot;
import com.agribusiness360.backend.model.SoilType;
import com.agribusiness360.backend.repository.PlotRepository;

@Service
public class PlotService {

    @Autowired
    private PlotRepository plotRepository;

    /**
     *  Retrieves all plots
     */
    public List<Plot> getAllPlots() {
        return plotRepository.findAll();
    }

    /**
     *  Retrieves plots belonging to a specific property
     */
    public List<Plot> getPlotsByProperty(Integer propertyId) {
        return plotRepository.findByRuralPropertyId(propertyId);
    }

    /**
     *  Retrieves plots based on the provided name
     */
    public List<Plot> getPlotsByName(String name) {
        return plotRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     *  Retrieves the plot using the unique identifier code
     */
    public Plot getPlotByUniqueCode(String code) {
        return plotRepository.findByCode(code)
            .orElseThrow(()-> new RuntimeException("plot not found for the specified unique verification code."));
    }

    /**
     *  Retrieves plots according to specific soil 
     */
    public List<Plot> getPlotsBySoilType(SoilType soilType) {
        return plotRepository.findBySoilType(soilType);
    }

    /**
     *  Saves a new plot
     */
    @Transactional
    public Plot savePlot(Plot plot) {
        if(plotRepository.findByCode(plot.getCode()).isPresent()) {
            throw new RuntimeException("This plot code is already in use.");
        }

        List<Plot> plotWithSameName = plotRepository.findByNameContainingIgnoreCase(plot.getName());

        for(Plot existingPlot : plotWithSameName) {
            if(existingPlot.getRuralProperty().getId().equals(plot.getRuralProperty().getId())) {
                throw new RuntimeException("This property already has an plot registered with this name.");
            }
        }

        return plotRepository.save(plot);
    }

    /**
     *  Update existing plot
     */
    @Transactional
    public Plot updatePlot(Integer id, Plot details) {
        Plot regdPlot = plotRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Plot not found."));

        if(!regdPlot.getCode().equals(details.getCode())) {
            throw new RuntimeException("The identification code cannot be changed.");
        }

        List<Plot> plotsInProperty = plotRepository.findByRuralPropertyId(regdPlot.getRuralProperty().getId());

        for(Plot existingPlot : plotsInProperty) {
            if(existingPlot.getName().equals(details.getName()) && !existingPlot.getId().equals(id)) {
                throw new RuntimeException("This property already has another plot registered with this name.");
            }
        }

        regdPlot.setName(details.getName());
        regdPlot.setSoilType(details.getSoilType());
        regdPlot.setArea(details.getArea());

        return plotRepository.save(regdPlot);
    }

    /**
     *  Delete a plot
     */
    public void deletePlot(Integer id) {
        if(!plotRepository.existsById(id)) {
             throw new RuntimeException("Cannot delete: Plot not found.");
        }

        plotRepository.deleteById(id);
    }
}
