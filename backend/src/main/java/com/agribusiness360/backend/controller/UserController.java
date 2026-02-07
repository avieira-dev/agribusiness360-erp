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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.agribusiness360.backend.model.User;
import com.agribusiness360.backend.service.UserService;

@RestController
@RequestMapping("/api/users")
/** SECURITY CONFIGURATION (CORS):
 *  - Currently configured to accept requests from the Vue/Vite Frontend 
 *  (localhost:5173).
 *  - IMPORTANT: For production environments, 
 *  replace "http://localhost:5173" with the official URL where your 
 *  Frontend is hosted.
 */
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *  Search for all users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    /**
     *  Search for a user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }

    /**
     *  Search for a user by username
     */
    @GetMapping("/search-username")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    /**
     *  Search for a user by email
     */
    @GetMapping("/search-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    /**
     *  Search for a user by code
     */
    @GetMapping("/search-code")
    public ResponseEntity<User> getUserByCode(@RequestParam String code) {
        return ResponseEntity.ok(userService.getByCode(code));
    }

    /**
     *  Create a new user
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     *  Update an existing user
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    /**
     *  Delete user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
