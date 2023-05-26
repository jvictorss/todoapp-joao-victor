package org.todoapplication.todo.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.todoapplication.todo.bo.UsuarioBO;
import org.todoapplication.todo.dto.UsuarioDTO;
import org.todoapplication.todo.dto.request.LoginRequest;
import org.todoapplication.todo.dto.response.LoginResponse;
import org.todoapplication.todo.exception.CantSaveException;
import org.todoapplication.todo.exception.PasswordInvalidException;
import org.todoapplication.todo.exception.TaskNotFoundException;
import org.todoapplication.todo.exception.UserNotFoundException;
import org.todoapplication.todo.security.JwtUtils;

import javax.validation.Valid;

@Api(value = "Usuário", description = "Funcionalidade que cria e faz login do usuário", tags = "Usuário")
@ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "User Not Found"),
        @ApiResponse(code = 500, message = "Can't Save"),
        @ApiResponse(code = 401, message = "Password Invalid")
})
@RestController
@RequestMapping(path = "/usuario")
public class UsuarioController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private final UsuarioBO usuarioBO;


    @Autowired
    public UsuarioController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UsuarioBO usuarioBO) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.usuarioBO = usuarioBO;
    }

    @ApiOperation(value = "Salvar um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 500, message = "Can't Save"),
            @ApiResponse(code = 401, message = "Password Invalid")
    })
    @PostMapping(path = "/salvar")
    public ResponseEntity<Void> salvarUsuario(@RequestBody UsuarioDTO usuarioDTO) throws UserNotFoundException,
            CantSaveException, PasswordInvalidException {
        return new ResponseEntity<>(usuarioBO.salvarUsuario(usuarioDTO), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Autenticar um usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping("/entrar")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new LoginResponse(jwt));
    }
}
