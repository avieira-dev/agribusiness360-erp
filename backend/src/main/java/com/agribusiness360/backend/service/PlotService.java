package com.agribusiness360.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import com.agribusiness360.backend.dto.PlotRequestDTO;
import com.agribusiness360.backend.dto.PlotResponseDTO;
import com.agribusiness360.backend.exception.BusinessException;
import com.agribusiness360.backend.exception.ResourceNotFoundException;
import com.agribusiness360.backend.model.RuralProperty;
import com.agribusiness360.backend.repository.RuralPropertyRepository;
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

    @Autowired
    private RuralPropertyRepository ruralPropertyRepository;

    /**
     *  Convert entity to DTO
     */
    private PlotResponseDTO toResponse(Plot plot) {
        return new PlotResponseDTO(
                plot.getId(),
                plot.getRuralProperty().getId(),
                plot.getRuralProperty().getName(),
                plot.getRuralProperty().getCode(),
                plot.getName(),
                plot.getCode(),
                plot.getArea(),
                plot.getSoilType(),
                plot.getCreatedAt()
        );
    }

    /**
     *  Convert DTO to entity
     */
    private Plot toEntity(PlotRequestDTO dto) {
        Plot plot = new Plot();

        if(dto.area().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Area cannot be negative.");
        }

        RuralProperty property = ruralPropertyRepository.findById(dto.propertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found."));

        plot.setRuralProperty(property);
        plot.setName(dto.name());
        plot.setCode(dto.code());
        plot.setArea(dto.area());
        plot.setSoilType(dto.soilType());

        return plot;
    }

    /**
     *  Retrieves all plots
     */
    @Transactional(readOnly = true)
    public List<PlotResponseDTO> getAllPlots() {
        return plotRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves a plot by ID
     */
    @Transactional(readOnly = true)
    public PlotResponseDTO getPlotById(Integer id) {
        Plot plot = plotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No plot found with this ID."));

        return toResponse(plot);
    }

    /**
     *  Retrieves plots belonging to a specific property
     */
    @Transactional(readOnly = true)
    public List<PlotResponseDTO> getPlotsByProperty(Integer propertyId) {
        return plotRepository.findByRuralPropertyId(propertyId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves plots based on the provided name
     */
    @Transactional(readOnly = true)
    public List<PlotResponseDTO> getPlotsByName(String name) {
        return plotRepository.findByNameContainingIgnoreCase(name).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves the plot using the unique identifier code
     */
    @Transactional(readOnly = true)
    public PlotResponseDTO getPlotByUniqueCode(String code) {
        Plot plot = plotRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Plot not found for the given code."));

        return toResponse(plot);
    }

    /**
     *  Retrieves plots according to specific soil 
     */
    @Transactional(readOnly = true)
    public List<PlotResponseDTO> getPlotsBySoilType(SoilType soilType) {
        return plotRepository.findBySoilType(soilType).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Saves a new plot
     */
    @Transactional
    public PlotResponseDTO savePlot(PlotRequestDTO dto) {
        if(plotRepository.existsByNameAndRuralPropertyId(dto.name(), dto.propertyId())) {
            throw new BusinessException("A plot with this name already exists in the property.");
        }

        if(plotRepository.existsByCodeAndRuralPropertyId(dto.code(), dto.propertyId())) {
            throw new BusinessException("This plot code is already in use.");
        }

        Plot plot = toEntity(dto);

        return toResponse(plotRepository.save(plot));
    }

    /**
     *  Update existing plot
     */
    @Transactional
    public PlotResponseDTO updatePlot(Integer id, PlotRequestDTO dto) {
        Plot plot = plotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No plot found with this ID."));

        plotRepository.findByNameAndRuralPropertyId(dto.name(), dto.propertyId()).ifPresent(existing -> {
            if(!existing.getId().equals(id)) {
                throw new BusinessException("This property already has another plot registered with this name.");
            }
        });

        if(!plot.getCode().equals(dto.code())) {
            throw new BusinessException("The identification code cannot be changed.");
        }

        if(!plot.getRuralProperty().getId().equals(dto.propertyId())) {
            throw new BusinessException("Cannot move a plot to another property.");
        }

        Plot updatedPlot = toEntity(dto);

        updatedPlot.setId(id);

        return toResponse(plotRepository.save(updatedPlot));
    }

    /**
     *  Delete a plot
     */
    public void deletePlot(Integer id) {
        if(!plotRepository.existsById(id)) {
             throw new BusinessException("Cannot delete: Plot not found.");
        }

        plotRepository.deleteById(id);
    }
}
