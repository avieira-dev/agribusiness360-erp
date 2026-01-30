package com.agribusiness360.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.agribusiness360.backend.model.Access;
import com.agribusiness360.backend.model.AccessId;
import com.agribusiness360.backend.model.PermissionLevel;
import com.agribusiness360.backend.model.RuralProperty;
import com.agribusiness360.backend.model.User;
import com.agribusiness360.backend.repository.AccessRepository;
import com.agribusiness360.backend.repository.RuralPropertyRepository;
import com.agribusiness360.backend.repository.UserRepository;

@Service
public class AccessService {

    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RuralPropertyRepository ruralPropertyRepository;

    /**
     *  Retrieves all access permissions in the system
     */
    public List<Access> getAllAccesses() {
        return accessRepository.findAll();
    }

    /**
     *  Retrieves all accesses for a specific user
     */
    public List<Access> getAccessesByUser(Integer id) {
        return accessRepository.findByIdUserId(id);
    }

    /**
     *  Retrieves all accesses for a specific rural property
     */
    public List<Access> getAccessesByRuralProperty(Integer id) {
        return accessRepository.findByIdPropertyId(id);
    }

    /**
     *  Grants a new access permission
     */
    @Transactional
    public Access grantAccess(Integer userId, Integer propertyId, PermissionLevel permissionLevel) {
        User user = userRepository.findById(userId)
            .orElseThrow(()-> new RuntimeException("User not found with the given ID."));

        RuralProperty ruralProperty = ruralPropertyRepository.findById(propertyId)
            .orElseThrow(()-> new RuntimeException("Rural property not found with the given ID."));

        AccessId accessId = new AccessId(userId, propertyId);

        if(accessRepository.existsById(accessId)) {
            throw new RuntimeException("This user already has an access level defined for this property.");
        }

        Access newAccess = new Access();

        newAccess.setId(accessId);
        newAccess.setUser(user);
        newAccess.setRuralProperty(ruralProperty);
        newAccess.setPermissionLevel(permissionLevel != null ? permissionLevel : PermissionLevel.LEITOR);

        return accessRepository.save(newAccess);
    }

    /**
     *  Updates the permission level of an existing access
     */
    @Transactional
    public Access updatePermission(Integer idUser, Integer idProperty, PermissionLevel newLevel) {
        AccessId accessId = new AccessId(idUser, idProperty);

        Access access = accessRepository.findById(accessId)
            .orElseThrow(()-> new RuntimeException("Access record not found to update."));

        access.setPermissionLevel(newLevel);

        return accessRepository.save(access);
    }

    /**
     *  Revokes access (deletes the association)
     */
    @Transactional
    public void revokeAccess(Integer idUser, Integer idProperty) {
        AccessId accessId = new AccessId(idUser, idProperty);

        if(!accessRepository.existsById(accessId)) {
            throw new RuntimeException("Cannot revoke: Access record not found.");
        }

        accessRepository.deleteById(accessId);
    }
}
