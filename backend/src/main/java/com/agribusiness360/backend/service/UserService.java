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
     *  Retrieves all registered users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     *  Retrieves the user identified by the given ID
     */
    public User getUserById(Integer id) {
        return userRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("User with the given ID does not exist."));
    }

    /**
     *  Retrieves the user with the given username
     */
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(()-> new RuntimeException("User with the given username does not exist."));
    }

    /**
     *  Retrieves the user with the given email
     */
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(()-> new RuntimeException("User with the given email does not exist."));
    }

    /**
     *  Retrieves the user with the given unique code
     */
    public User getByCode(String code) {
        return userRepository.findByCode(code)
            .orElseThrow(()-> new RuntimeException("User with the given code does not exist."));
    }

    /**
     *  Saves a new user
     */
    @Transactional
    public User saveUser(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User with the given email already exists.");
        }

        if(userRepository.findByCode(user.getCode()).isPresent()) {
            throw new RuntimeException("User with the given code already exists.");
        }

        return userRepository.save(user);
    }

    /**
     *  Deletes the user with the given ID
     */
    @Transactional
    public void deleteUser(Integer id) {
        if(!userRepository.existsById(id)) {
            throw new RuntimeException("User with the given ID does not exist.");
        }

        userRepository.deleteById(id);
    }
}
