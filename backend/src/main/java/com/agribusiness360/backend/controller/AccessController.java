package com.agribusiness360.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.agribusiness360.backend.model.Access;
import com.agribusiness360.backend.model.PermissionLevel;
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
    public ResponseEntity<List<Access>> getAllAccess() {
        List<Access> accesses = accessService.getAllAccesses();

        return ResponseEntity.ok(accesses);
    }

    /**
     *  Search a specific user’s access
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Access>> getAccessByUser(@PathVariable Integer id) {
        List<Access> accesses = accessService.getAccessesByUser(id);

        return ResponseEntity.ok(accesses);
    }

    /**
     *  Search the access levels of users for a specific property
     */
    @GetMapping("/property/{id}")
    public ResponseEntity<List<Access>> getAccessByProperty(@PathVariable Integer id) {
        List<Access> accesses = accessService.getAccessesByRuralProperty(id);

        return ResponseEntity.ok(accesses);
    }

    /**
     *  Grants a new access permission
     */
    @PostMapping("/grant-access")
    public ResponseEntity<Access> grantAccess(@RequestParam Integer userId, @RequestParam Integer propertyId, @RequestParam PermissionLevel level) {
        return ResponseEntity.ok(accessService.grantAccess(userId, propertyId, level));
    }

    /**
     *  Updates the permission level of an existing access
     */
    @PutMapping("/update-permission")
    public ResponseEntity<Access> updatePermission(@RequestParam Integer userId, @RequestParam Integer propertyId, @RequestParam PermissionLevel newLevel) {
        return ResponseEntity.ok(accessService.updatePermission(userId, propertyId, newLevel));
    }

    /**
     *  Revokes access
     */
    @DeleteMapping("/revoke-access")
    public ResponseEntity<Void> revokeAccess(@RequestParam Integer userId, @RequestParam Integer propertyId) {
        accessService.revokeAccess(userId, propertyId);

        return ResponseEntity.noContent().build();
    }
}
