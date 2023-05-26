package org.todoapplication.todo.bo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.todoapplication.todo.entity.Usuario;
import org.todoapplication.todo.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsBOTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsBO userDetailsBO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_ExistingUser_ReturnsUserDetails() {
        // Arrange
        String email = "test@example.com";
        Usuario user = new Usuario();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Usuario result = userDetailsBO.loadUserByUsername(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_NonExistingUser_ThrowsException() {
        // Arrange
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsBO.loadUserByUsername(email));
        verify(userRepository, times(1)).findByEmail(email);
    }
}