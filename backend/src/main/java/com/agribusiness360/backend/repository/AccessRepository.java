package com.agribusiness360.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agribusiness360.backend.model.Access;
import com.agribusiness360.backend.model.AccessId;

@Repository
public interface AccessRepository extends JpaRepository<Access, AccessId> {

    /**
     * Find all access permissions for a specific user.
     */
    List<Access> findByIdUserId(Integer userId);

    /**
     * Find all users who have access to a specific 
     * property.
     */
    List<Access> findByIdPropertyId(Integer propertyId);
}
