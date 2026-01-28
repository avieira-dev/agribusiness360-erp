package com.agribusiness360.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.agribusiness360.backend.model.RuralProperty;
import com.agribusiness360.backend.repository.RuralPropertyRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RuralPropertyService {

    @Autowired
    private RuralPropertyRepository ruralPropertyRepository;

    /**
     *  Retrieves all registered rural properties
     */
    public List<RuralProperty> getAllRuralProperties() {
        return ruralPropertyRepository.findAll();
    }

    /**
     *  Retrieves the rural property associated with the given ID
     */
    public RuralProperty getRuralPropertyId(Integer id) {
        return ruralPropertyRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Rural property with the given ID does not exist."));
    }

    /**
     *  Retrieves the rural property identified by the given name
     */
    public RuralProperty getByName(String name) {
        return ruralPropertyRepository.findByName(name)
            .orElseThrow(()-> new RuntimeException("Rural property with the given name does not exist."));
    }

    /**
     *  Retrieves the rural property identified by the given
     *  unique verification code
     */
    public RuralProperty getByCode(String code) {
        return ruralPropertyRepository.findByCode(code)
            .orElseThrow(()-> new RuntimeException("Rural property not found for the specified unique verification code."));
    }

    /**
     *  Saves the given rural property
     */
    @Transactional
    public RuralProperty saveRuralProperty(RuralProperty ruralProperty) {
        if(ruralPropertyRepository.findByName(ruralProperty.getName()).isPresent()) {
            throw new RuntimeException("A rural property with this name is already registered.");
        }

        if(ruralPropertyRepository.findByCode(ruralProperty.getCode()).isPresent()) {
            throw new RuntimeException("This verification code is already in use by another property.");
        }

        return ruralPropertyRepository.save(ruralProperty);
    }

    /**
     *  Deletes the rural property associated with the given ID
     */
    @Transactional
    public void deleteRuralProperty(Integer id) {
        if(!ruralPropertyRepository.existsById(id)) {
            throw new RuntimeException("Rural property not found for the given ID.");
        }

        ruralPropertyRepository.deleteById(id);
    }
}
