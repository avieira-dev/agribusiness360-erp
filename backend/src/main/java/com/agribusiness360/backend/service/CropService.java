package com.agribusiness360.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.agribusiness360.backend.dto.CropRequestDTO;
import com.agribusiness360.backend.dto.CropResponseDTO;
import com.agribusiness360.backend.exception.BusinessException;
import com.agribusiness360.backend.exception.ResourceNotFoundException;
import com.agribusiness360.backend.model.Crop;
import com.agribusiness360.backend.model.CropStatus;
import com.agribusiness360.backend.model.CultureType;
import com.agribusiness360.backend.model.Plot;
import com.agribusiness360.backend.repository.CropRepository;
import com.agribusiness360.backend.repository.PlotRepository;

@Service
public class CropService {
    
    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private PlotRepository plotRepository;

    /**
     *  Convert entity to DTO
    */
   private CropResponseDTO toResponse(Crop crop) {
        return new CropResponseDTO(
            crop.getId(),
            crop.getBasePrice(),
            crop.getProductStatus(),
            crop.getPlot().getId(),
            crop.getPlot().getName(),
            crop.getName(),
            crop.getExpectedYield(),
            crop.getCultureType(),
            crop.getSpecificCulture(),
            crop.getStatus(),
            crop.getPlantDate(),
            crop.getHarvestDate(),
            crop.getCreatedAt()
        );
   }

   /** 
    *   Convert DTO to entity
    */
   private Crop toEntity(CropRequestDTO dto, Plot plot) {
        Crop crop = new Crop();

        crop.setPlot(plot);
        crop.setBasePrice(dto.basePrice());
        crop.setProductStatus(dto.productStatus());
        crop.setName(dto.name());
        crop.setExpectedYield(dto.expectedYield());
        crop.setCultureType(dto.cultureType());
        crop.setSpecificCulture(dto.specificCulture());
        crop.setStatus(dto.status());
        crop.setPlantDate(dto.plantDate());
        crop.setHarvestDate(dto.harvestDate());

        return crop;
   }

   /**
    *   Business validations for creation and updating
    */
   private void validateCropData(CropRequestDTO dto) {
        if(dto.specificCulture() == null || dto.specificCulture().isBlank()) {
            throw new BusinessException("The field of specific culture cannot be empty.");
        }

        if(dto.expectedYield() != null && dto.expectedYield().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Expected yield cannot be negative.");
        } 

        if(dto.plantDate() != null && dto.harvestDate() != null) {
            if(dto.harvestDate().isBefore(dto.plantDate())) {
                throw new BusinessException("Harvest date cannot be before plant date.");
            }
        }

        if(dto.basePrice() != null && dto.basePrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("The base price cannot be negative.");
        }
   }

    /**
     *  Retrieves all crops
     */
    @Transactional(readOnly = true)
    public List<CropResponseDTO> getAllCrops() {
        return cropRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves a crops of a specific plot
     */
    @Transactional(readOnly = true)
    public List<CropResponseDTO> getCropsByPlotId(Integer plotId) {
        if(!plotRepository.existsById(plotId)) {
            throw new ResourceNotFoundException("No plot found with the ID provided.");
        }

        return cropRepository.findByPlotId(plotId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves a crops by specific name
     */
    @Transactional(readOnly = true)
    public List<CropResponseDTO> getCropByName(String name) {
        if(cropRepository.findByNameContainingIgnoreCase(name).isEmpty()) {
            throw new BusinessException("No crops found with this name.");
        }

        return cropRepository.findByNameContainingIgnoreCase(name).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves a crops by culture type
     */
    @Transactional(readOnly = true)
    public List<CropResponseDTO> getCropsByCultureType(CultureType cultureType) {
        if(cropRepository.findByCultureType(cultureType).isEmpty()) {
            throw new BusinessException("No plantations were found with that type of crop.");
        }

        return cropRepository.findByCultureType(cultureType).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves a crops by specific culture
     */
    @Transactional(readOnly = true)
    public List<CropResponseDTO> getCropsSpecificCulture(String specificCulture) {
        if(cropRepository.findBySpecificCultureContainingIgnoreCase(specificCulture).isEmpty()) {
            throw new BusinessException("No plantations were found with that specific type of crop.");
        }

        return cropRepository.findBySpecificCultureContainingIgnoreCase(specificCulture).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves a crops by status
     */
    @Transactional(readOnly = true)
    public List<CropResponseDTO> getCropsByStatus(CropStatus cropStatus) {
        if(cropRepository.findByStatus(cropStatus).isEmpty()) {
            throw new BusinessException("No crops found with this status.");
        }

        return cropRepository.findByStatus(cropStatus).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves crops within the specified date range
     */
    @Transactional(readOnly = true)
    public List<CropResponseDTO> getCropsByPlantDateBetween(LocalDate startDate, LocalDate endDate) {
        if(startDate == null || endDate == null) {
            throw new BusinessException("The date cannot be empty.");
        }

        LocalDate firstDate = (startDate.isBefore(endDate)) ? startDate : endDate;
        LocalDate lastDate = (startDate.isAfter(endDate)) ? startDate : endDate;


        return cropRepository.findByPlantDateBetween(firstDate, lastDate).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Saves a new crop, creating the associated product first
     */
    @Transactional
    public CropResponseDTO saveCrop(CropRequestDTO dto) {
        Plot plot = plotRepository.findById(dto.plotId())
            .orElseThrow(() -> new ResourceNotFoundException("No plots were found with the provided ID."));

        validateCropData(dto);

        Crop crop = toEntity(dto, plot);

        return toResponse(cropRepository.save(crop));
    }

    /**
     *  Updates an existing crop and its associated base product information
     */
    @Transactional
    public CropResponseDTO updateCrop(Integer id, CropRequestDTO dto) {
        Crop existingCrop = cropRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Crop not found with the provided ID."));

        Plot plot = plotRepository.findById(dto.plotId())
            .orElseThrow(() -> new ResourceNotFoundException("No plot were found with that ID."));

        if(!existingCrop.getPlot().getId().equals(plot.getId())) {
            throw new BusinessException("The crop cannot be moved to another plot.");
        }

        validateCropData(dto);

        Crop crop = toEntity(dto, plot);

        crop.setId(id);

        return toResponse(cropRepository.save(crop));
    }

    /**
     *  Delete a crop
     */
    @Transactional
    public void deleteCrop(Integer id) {
        if(!cropRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete: Crop record not found.");
        }

        cropRepository.deleteById(id);
    }
}
