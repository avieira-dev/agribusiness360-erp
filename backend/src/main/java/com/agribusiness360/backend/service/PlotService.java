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
     *  Updates entity data based on the DTO
     */
    private void updateEntityFromDto(Plot plot, PlotRequestDTO dto, RuralProperty property) {
        plot.setRuralProperty(property);
        plot.setName(dto.name());
        plot.setCode(dto.code());
        plot.setArea(dto.area());
        plot.setSoilType(dto.soilType());
    }

    /**
     *  Business validations
     */
    private void validatePlotData(PlotRequestDTO dto) {
        if (dto.area() == null || dto.area().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Area cannot be null or negative.");
        }
    }

    /**
     *  Retrieves all plots
     */
    @Transactional(readOnly = true)
    public List<PlotResponseDTO> getAllPlots() {
        return plotRepository.findAllWithProperty().stream().map(this::toResponse).collect(Collectors.toList());
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
        validatePlotData(dto);

        RuralProperty property = ruralPropertyRepository.findById(dto.propertyId())
            .orElseThrow(() -> new ResourceNotFoundException("Property not found."));

        if (plotRepository.existsByNameAndRuralPropertyId(dto.name(), dto.propertyId())) {
            throw new BusinessException("A plot with this name already exists in this property.");
        }

        if (plotRepository.existsByCodeAndRuralPropertyId(dto.code(), dto.propertyId())) {
            throw new BusinessException("This plot code is already in use in this property.");
        }

        Plot plot = new Plot();
        updateEntityFromDto(plot, dto, property);
        return toResponse(plotRepository.save(plot));
    }

    /**
     *  Update existing plot
     */
    @Transactional
    public PlotResponseDTO updatePlot(Integer id, PlotRequestDTO dto) {
        validatePlotData(dto);

        Plot plot = plotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plot not found."));

        if (!plot.getRuralProperty().getId().equals(dto.propertyId())) {
            throw new BusinessException("A plot cannot be moved to another property.");
        }

        if (!plot.getCode().equals(dto.code())) {
            throw new BusinessException("The plot identification code cannot be changed.");
        }

        if (!plot.getName().equals(dto.name()) && 
            plotRepository.existsByNameAndRuralPropertyId(dto.name(), dto.propertyId())) {
            throw new BusinessException("Another plot already exists with this name in this property.");
        }

        RuralProperty property = plot.getRuralProperty();

        updateEntityFromDto(plot, dto, property);

        return toResponse(plotRepository.save(plot));
    }

    /**
     *  Delete a plot
     */
    public void deletePlot(Integer id) {
        if (!plotRepository.existsById(id)) {
             throw new ResourceNotFoundException("Cannot delete: Plot not found.");
        }
        plotRepository.deleteById(id);
    }
}