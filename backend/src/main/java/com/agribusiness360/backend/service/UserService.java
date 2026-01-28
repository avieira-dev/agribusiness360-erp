package com.agribusiness360.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.agribusiness360.backend.model.User;
import com.agribusiness360.backend.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     *  Returns all registered users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     *  Search for a user by ID
     */
    public User getUserById(Integer id) {
        return userRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("User with ID " + id + " not found."));
    }

    /**
     *  Search for a user by username
     */
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(()-> new RuntimeException("The user " + username + " it was not found."));
    }

    /**
     *  Search for a user by email
     */
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(()-> new RuntimeException("No user was found with this email."));
    }

    /**
     *  Search for a user by unique code
     */
    public User getByCode(String code) {
        return userRepository.findByCode(code)
            .orElseThrow(()-> new RuntimeException("No users registered with this code."));
    }

    /**
     *  Register a new user
     */
    @Transactional
    public User saveUser(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("This email address is already registered.");
        }

        return userRepository.save(user);
    }

    /**
     *  Delete a user by ID
     */
    @Transactional
    public void deleteUser(Integer id) {
        if(!userRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: User with Id " + id + " not found.");
        }

        userRepository.deleteById(id);
    }
}
