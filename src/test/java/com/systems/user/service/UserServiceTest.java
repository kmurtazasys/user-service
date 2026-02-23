package com.systems.user.service;

import com.systems.user.entity.User;
import com.systems.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void loadUserByUsername_Success() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        
        var result = userService.loadUserByUsername("test");
        
        assertNotNull(result);
        assertEquals("test", result.getUsername());
        verify(userRepository).findByUsername("test");
    }
    
    @Test
    void loadUserByUsername_NotFound() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());
        
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("test"));
    }
}
