package org.todoapplication.todo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.todoapplication.todo.entity.Usuario;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.email =?1")
    Optional<Usuario> findByEmail(String email);


    @Query("SELECT u FROM Usuario u WHERE u.email =?1")
    Optional<Usuario> existsUserByEmail(String email);

}
