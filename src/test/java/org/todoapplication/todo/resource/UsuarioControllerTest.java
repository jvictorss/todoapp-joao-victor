package org.todoapplication.todo.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.todoapplication.todo.bo.UsuarioBO;
import org.todoapplication.todo.dto.UsuarioDTO;
import org.todoapplication.todo.dto.request.LoginRequest;
import org.todoapplication.todo.dto.response.LoginResponse;
import org.todoapplication.todo.exception.CantSaveException;
import org.todoapplication.todo.exception.PasswordInvalidException;
import org.todoapplication.todo.exception.UserNotFoundException;
import org.todoapplication.todo.security.JwtUtils;

import javax.validation.Valid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UsuarioBO usuarioBO;

    @Mock
    private LoginResponse loginResponse;

    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioController = new UsuarioController(authenticationManager, jwtUtils, usuarioBO);
    }

    @Test
    void salvarUsuario_WhenValidUsuarioDTO_ReturnsCreatedResponse() throws UserNotFoundException,
            CantSaveException, PasswordInvalidException {
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        ResponseEntity<Void> response = usuarioController.salvarUsuario(usuarioDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(usuarioBO, times(1)).salvarUsuario(usuarioDTO);
    }

    @Test
    void authenticateUser_WhenValidLoginRequest_ReturnsOkResponse() {
        LoginRequest loginRequest = new LoginRequest();
        Authentication authentication = mock(Authentication.class);
        String jwt = "test-token";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwt);

        ResponseEntity<?> response = usuarioController.authenticateUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateJwtToken(authentication);
    }
}
