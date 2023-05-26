package org.todoapplication.todo.bo;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.todoapplication.todo.dto.UsuarioDTO;
import org.todoapplication.todo.entity.Usuario;
import org.todoapplication.todo.exception.CantSaveException;
import org.todoapplication.todo.exception.PasswordInvalidException;
import org.todoapplication.todo.exception.UserNotFoundException;
import org.todoapplication.todo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.todoapplication.todo.constants.TodoConstants.*;

@Component
public class UsuarioBO {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    public UsuarioBO(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.mapper = new ModelMapper();
    }

    public Void salvarUsuario(UsuarioDTO usuarioDTO) throws CantSaveException, UserNotFoundException, PasswordInvalidException {
        Optional<Usuario> user = pesquisarEmail(usuarioDTO.getEmail());
        if(user.isPresent()) {
            throw new CantSaveException(ERROR_CONFLICT_SAVE_USER);
        }
        if(!validatePassword(usuarioDTO.getSenha())){
            throw new PasswordInvalidException(INVALID_PASSWORD_EXCEPTION);
        }
        Usuario usuarioEntity = mapper.map(usuarioDTO, Usuario.class);
        usuarioEntity.setCriado(LocalDateTime.now());
        usuarioEntity.setSenha(new BCryptPasswordEncoder().encode(usuarioDTO.getSenha()));

        try {
            userRepository.save(usuarioEntity);
        } catch (Exception e) {
            throw new CantSaveException(ERROR_SAVE_USER);
        }
        return null;
    }

    Optional<Usuario> pesquisarEmail(String email) throws UserNotFoundException {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception ex) {
            throw new UserNotFoundException(ERROR_USER_NOT_FOUND);
        }
    }

    public static boolean validatePassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&*]).+$";

        if (!password.matches(pattern)) {
            return false;
        }
        String encryptedPassword = passwordEncoder.encode(password);
        return passwordEncoder.matches(password, encryptedPassword);
    }
}
