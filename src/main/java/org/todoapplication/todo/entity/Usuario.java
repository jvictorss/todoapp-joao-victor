package org.todoapplication.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Table(name = "usuario")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "sq_usuario_id",
        sequenceName = "sq_usuario_id",
        allocationSize = 1,
        initialValue = 1
)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_usuario_id")
    @Column(name = "usuario_id")
    private Long id;

    @Column(name = "usuario_nome")
    @NotNull
    @NotEmpty
    private String nome;

    @Column(name = "usuario_email")
    @NotNull
    @NotEmpty
    private String email;

    @Column(name = "usuario_senha")
    @NotNull
    @NotEmpty
    private String senha;
    
    @Column(name = "usuario_criado")
    private LocalDateTime criado;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
