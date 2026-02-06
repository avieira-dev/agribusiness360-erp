package com.agribusiness360.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.agribusiness360.backend.model.Crop;
import com.agribusiness360.backend.model.CropStatus;
import com.agribusiness360.backend.model.CultureType;
import com.agribusiness360.backend.repository.CropRepository;
import com.agribusiness360.backend.repository.PlotRepository;

@Service
public class CropService {
    
    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private PlotRepository plotRepository;

    @Autowired
    private ProductService productService;

    /**
     *  Retrieves all crops
     */
    public List<Crop> getAllCrops() {
        return cropRepository.findAll();
    }

    /**
     *  Retrieves a crops of a specific plot
     */
    public List<Crop> getCropsByPlotId(Integer plotId) {
        if(!plotRepository.findById(plotId).isPresent()) {
            throw new RuntimeException("No plot found with the ID provided.");
        }

        return cropRepository.findByPlotId(plotId);
    }

    /**
     *  Retrieves a crops by specific name
     */
    public List<Crop> getCropByName(String name) {
        return cropRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     *  Retrieves a crops by specific culture type
     */
    public List<Crop> getCropsByCultureType(CultureType cultureType) {
        return cropRepository.findByCultureType(cultureType);
    }

    /**
     *  Retrieves a crops by specific culture
     */
    public List<Crop> getCropsSpecificCulture(String specificCulture) {
        return cropRepository.findBySpecificCultureContainingIgnoreCase(specificCulture);
    }

    /**
     *  Retrieves a crops by status
     */
    public List<Crop> getCropsByStatus(CropStatus cropStatus) {
        return cropRepository.findByStatus(cropStatus);
    }

    /**
     *  Retrieves crops within the specified date range
     */
    public List<Crop> getCropsByPlantDateBetween(LocalDate starDate, LocalDate endDate) {
        return cropRepository.findByPlantDateBetween(starDate, endDate);
    }

    /**
     *  Saves a new crop, creating the associated product first
     */
    @Transactional
    public Crop saveCrop(Crop crop) {
        if(crop.getPlantDate() != null && crop.getHarvestDate() != null) {
            if(crop.getHarvestDate().isBefore(crop.getPlantDate())) {
                throw new RuntimeException("Harvest date cannot be before plant date.");
            }
        }

        if(crop.getExpectedYield() != null && crop.getExpectedYield().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Expected yield cannot be negative.");
        }

        return cropRepository.save(crop);
    }

    /**
     *  Updates an existing crop and its associated base product information
     */
    @Transactional
    public Crop updateCrop(Integer id, Crop details) {
        Crop existingCrop = cropRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Crop not found with the provided ID."));

        if(details.getPlantDate() != null && details.getHarvestDate() != null) {
            if(details.getHarvestDate().isBefore(details.getPlantDate())) {
                throw new RuntimeException("Update failed: Harvest date cannot be before plant date.");
            }
        }

        existingCrop.setName(details.getName());
        existingCrop.setExpectedYield(details.getExpectedYield());
        existingCrop.setCultureType(details.getCultureType());
        existingCrop.setSpecificCulture(details.getSpecificCulture());
        existingCrop.setStatus(details.getStatus());
        existingCrop.setPlantDate(details.getPlantDate());
        existingCrop.setHarvestDate(details.getHarvestDate());
        existingCrop.setBasePrice(details.getBasePrice());
        existingCrop.setProductStatus(details.getProductStatus());

        return cropRepository.save(existingCrop);
    }

    /**
     *  Delete a crop
     */
    @Transactional
    public void deleteCrop(Integer id) {
        if(!cropRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Crop record not found.");
        }

        productService.deleteProduct(id);
    }
}
