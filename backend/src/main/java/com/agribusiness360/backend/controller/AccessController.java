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
import org.springframework.web.bind.annotation.RestController;
import com.agribusiness360.backend.dto.AccessRequestDTO;
import com.agribusiness360.backend.dto.AccessResponseDTO;
import com.agribusiness360.backend.service.AccessService;

@RestController
@RequestMapping("/api/access")
/** SECURITY CONFIGURATION (CORS):
 *  - Currently configured to accept requests from the Vue/Vite Frontend 
 *  (localhost:5173).
 *  - IMPORTANT: For production environments, 
 *  replace "http://localhost:5173" with the official URL where your 
 *  Frontend is hosted.
 */
@CrossOrigin(origins = "http://localhost:5173")
public class AccessController {

    @Autowired
    private AccessService accessService;

    /**
     *  Search for all access permissions
     */
    @GetMapping
    public ResponseEntity<List<AccessResponseDTO>> getAllAccess() {
        return ResponseEntity.ok(accessService.getAllAccesses());
    }

    /**
     *  Search a specific user’s access
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<AccessResponseDTO>> getAccessByUser(@PathVariable Integer id) {
        return ResponseEntity.ok(accessService.getAccessesByUser(id));
    }

    /**
     *  Search the access levels of users for a specific property
     */
    @GetMapping("/property/{id}")
    public ResponseEntity<List<AccessResponseDTO>> getAccessByProperty(@PathVariable Integer id) {
        return ResponseEntity.ok(accessService.getAccessesByRuralProperty(id));
    }

    /**
     *  Grants a new access permission
     */
    @PostMapping
    public ResponseEntity<AccessResponseDTO> grantAccess(@RequestBody AccessRequestDTO dto) {
        AccessResponseDTO access = accessService.grantAccess(dto);

        return new ResponseEntity<>(access, HttpStatus.CREATED);
    }

    /**
     *  Updates the permission level of an existing access
     */
    @PutMapping
    public ResponseEntity<AccessResponseDTO> updatePermission(@RequestBody AccessRequestDTO dto) {
        return ResponseEntity.ok(accessService.updatePermission(dto));
    }

    /**
     *  Revokes access
     */
    @DeleteMapping
    public ResponseEntity<Void> revokeAccess(@RequestBody AccessRequestDTO dto) {
        accessService.revokeAccess(dto);

        return ResponseEntity.noContent().build();
    }
}
