package org.todoapplication.todo.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.todoapplication.todo.entity.Usuario;
import org.todoapplication.todo.repository.UserRepository;

import java.util.Optional;

@Component
public class UserDetailsBO implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Usuario loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> user = userRepository.findByEmail(email);

        user.orElseThrow(() -> new UsernameNotFoundException(email + " não encontrado."));

        return user.get();
    }
}
