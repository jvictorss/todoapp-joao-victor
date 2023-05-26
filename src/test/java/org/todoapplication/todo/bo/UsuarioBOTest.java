package org.todoapplication.todo.bo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.todoapplication.todo.dto.UsuarioDTO;
import org.todoapplication.todo.entity.Usuario;
import org.todoapplication.todo.exception.CantSaveException;
import org.todoapplication.todo.exception.PasswordInvalidException;
import org.todoapplication.todo.exception.UserNotFoundException;
import org.todoapplication.todo.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioBOTest {

    @Mock
    private UserRepository userRepository;

    private UsuarioBO usuarioBO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioBO = new UsuarioBO(userRepository);
    }

    @Test
    void salvarUsuario_WithNewUser_ShouldSaveUser() throws CantSaveException, UserNotFoundException, PasswordInvalidException {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setEmail("test@example.com");
        usuarioDTO.setSenha("Abc123!@#");

        when(userRepository.findByEmail(usuarioDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(Usuario.class))).thenReturn(new Usuario());

        Void result = usuarioBO.salvarUsuario(usuarioDTO);

        assertNull(result);
        verify(userRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void salvarUsuario_WithExistingUser_ShouldThrowCantSaveException() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setEmail("existing@example.com");
        usuarioDTO.setSenha("Abc123!@#");

        when(userRepository.findByEmail(usuarioDTO.getEmail())).thenReturn(Optional.of(new Usuario()));

        assertThrows(CantSaveException.class, () -> usuarioBO.salvarUsuario(usuarioDTO));
    }

    @Test
    void salvarUsuario_WithInvalidPassword_ShouldThrowPasswordInvalidException() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setEmail("test@example.com");
        usuarioDTO.setSenha("invalidpassword");

        assertThrows(PasswordInvalidException.class, () -> usuarioBO.salvarUsuario(usuarioDTO));
    }

    @Test
    void pesquisarEmail_WithExistingEmail_ShouldReturnOptionalUser() throws UserNotFoundException {
        String email = "test@example.com";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioBO.pesquisarEmail(email);

        assertTrue(result.isPresent());
        assertEquals(usuario, result.get());
    }

    @Test
    void validatePassword_WithValidPassword_ShouldReturnTrue() {
        String validPassword = "Abc123!@#";

        boolean result = UsuarioBO.validatePassword(validPassword);

        assertTrue(result);
    }

    @Test
    void validatePassword_WithInvalidPassword_ShouldReturnFalse() {
        String invalidPassword = "password123";

        boolean result = usuarioBO.validatePassword(invalidPassword);

        assertFalse(result);
    }
}