package com.systems.user.service;

import com.systems.user.dto.CreateUserRequest;
import com.systems.user.dto.RegisterRequest;
import com.systems.user.dto.UpdateUserRequest;
import com.systems.user.dto.UserDto;
import com.systems.user.entity.Role;
import com.systems.user.entity.User;
import com.systems.user.exception.BusinessException;
import com.systems.user.exception.ErrorCode;
import com.systems.user.repository.RoleRepository;
import com.systems.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                log.error("User not found: {}", username);
                return new UsernameNotFoundException("User not found: " + username);
            });
        
        log.debug("User loaded successfully: {}", username);
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName())
                .toArray(String[]::new))
            .build();
    }
    
    @Cacheable("users")
    public UserDto getUserByUsername(String username) {
        log.info("Fetching user details: {}", username);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                log.error("User not found: {}", username);
                return new BusinessException(ErrorCode.USER_NOT_FOUND, "User not found: " + username);
            });
        
        log.debug("User details fetched from cache or DB: {}", username);
        return toDto(user);
    }
    
    public List<UserDto> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
            .map(this::toDto)
            .toList();
    }
    
    public UserDto getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        User user = userRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "User not found with ID: " + id));
        return toDto(user);
    }
    
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserDto registerUser(RegisterRequest request) {
        log.info("Registering new user: {}", request.username());
        
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS, "Username already exists: " + request.username());
        }
        
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(true);
        
        // Assign default USER role (assuming role with ID 1 is USER)
        Role userRole = roleRepository.findById(1L)
            .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_ERROR, "Default USER role not found"));
        user.setRoles(Set.of(userRole));
        
        user = userRepository.save(user);
        log.info("User registered successfully: {}", user.getUsername());
        return toDto(user);
    }
    
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserDto createUser(CreateUserRequest request) {
        log.info("Creating user: {}", request.username());
        
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS, "Username already exists: " + request.username());
        }
        
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(true);
        
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.roleIds()));
        user.setRoles(roles);
        
        user = userRepository.save(user);
        log.info("User created successfully: {}", user.getUsername());
        return toDto(user);
    }
    
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserDto updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user ID: {}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "User not found with ID: " + id));
        
        if (request.password() != null) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        
        if (request.roleIds() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.roleIds()));
            user.setRoles(roles);
        }
        
        if (request.enabled() != null) {
            user.setEnabled(request.enabled());
        }
        
        user = userRepository.save(user);
        log.info("User updated successfully: {}", user.getUsername());
        return toDto(user);
    }
    
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long id) {
        log.info("Deleting user ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "User not found with ID: " + id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully: {}", id);
    }
    
    private UserDto toDto(User user) {
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }
}
