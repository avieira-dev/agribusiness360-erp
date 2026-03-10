package com.agribusiness360.backend.service;

import java.util.List;
import java.util.stream.Collectors;
import com.agribusiness360.backend.dto.UserRequestDTO;
import com.agribusiness360.backend.dto.UserResponseDTO;
import com.agribusiness360.backend.exception.BusinessException;
import com.agribusiness360.backend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.agribusiness360.backend.model.User;
import com.agribusiness360.backend.model.UserRole;
import com.agribusiness360.backend.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     *  Convert entity to ResponseDTO
     */
    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getCode(),
                user.getUserRole(),
                user.getCreatedAt()
        );
    }

    /**
     *  Convert DTO to entity
     */
    private User toEntity(UserRequestDTO dto) {
        User user = new User();

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPasswordHash(dto.password());
        user.setUsername(dto.username());
        user.setCode(dto.code());
        user.setUserRole(dto.userRole());

        return user;
    }

    /**
     *  Performs business validations on user data
     */
    private void validateUserData(UserRequestDTO dto) {
        if(dto.userRole() == null) {
            throw new BusinessException("The user's function cannot be null.");
        }
    }

    /**
     *  Retrieves all registered users
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     *  Retrieves the user identified by the given ID
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No user found with this ID."));

        return toResponse(user);
    }

    /**
     *  Retrieves the user with the given username
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("No user found with this name."));

        return toResponse(user);
    }

    /**
     *  Retrieves the user with the given email
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User with the given email does not exist."));

        return toResponse(user);
    }

    /**
     *  Retrieves the user with the given unique code
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getByCode(String code) {
        User user = userRepository.findByCode(code)
                .orElseThrow(()-> new ResourceNotFoundException("User with the given code does not exist."));

        return toResponse(user);
    }

    /**
     *  Saves a new user
     */
    @Transactional
    public UserResponseDTO saveUser(UserRequestDTO dto) {
        if(userRepository.findByEmail(dto.email()).isPresent()) {
            throw new BusinessException("This email is already registered.");
        } else if(userRepository.findByCode(dto.code()).isPresent()) {
            throw new BusinessException("This code already belongs to another user.");
        } else if(userRepository.findByUsername(dto.username()).isPresent()) {
            throw new BusinessException("A user with this username already exists");
        }

        validateUserData(dto);

        User user = toEntity(dto);

        return toResponse(userRepository.save(user));
    }

    /** 
     *  Update an existing user
     */
    @Transactional
    public UserResponseDTO updateUser(Integer id, UserRequestDTO dto) {
        if(!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with the given ID does not exist.");
        }

        userRepository.findByEmail(dto.email()).ifPresent(existingUser -> {
            if(!existingUser.getId().equals(id)) {
                throw new BusinessException("This email is already in use by another user.");
            }
        });

        userRepository.findByUsername(dto.username()).ifPresent(existingUser -> {
            if(!existingUser.getId().equals(id)) {
                throw new BusinessException("This username is already being used by another user.");
            }
        });

        userRepository.findByCode(dto.code()).ifPresent(existingUser -> {
            if(!existingUser.getId().equals(id)) {
                throw new BusinessException("This code is already being used by another user.");
            }
        });

        validateUserData(dto);

        User user = toEntity(dto);

        user.setId(id);

        return toResponse(userRepository.save(user));
    }

    /**
     *  Deletes the user with the given ID
     */
    @Transactional
    public void deleteUser(Integer id) {
        User userExisting = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with this ID."));

        if(UserRole.ADMINISTRADOR.equals(userExisting.getUserRole())) {
            throw new BusinessException("For security reasons, users with the ADMINISTRATOR profile " +
                    "cannot be removed via the standard API");
        }

        userRepository.delete(userExisting);
    }
}
