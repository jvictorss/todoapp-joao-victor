package org.todoapplication.todo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "tb_todo")
@SequenceGenerator(
        name = "sq_todo_id",
        sequenceName = "sq_todo_id",
        allocationSize = 1,
        initialValue = 1
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "todo_descricao")
    private String descricao;

    @Column(name = "todo_prioridade")
    private String prioridade;

    @Column(name = "todo_concluido")
    private Boolean concluido = Boolean.FALSE;

    @Column(name = "todo_usuarioCriou")
    private Long idUsuario;

    @Column(name = "todo_data_criacao")
    private LocalDateTime criado = LocalDateTime.now();

    @Column(name = "todo_data_atualizacao")
    private LocalDateTime atualizado = LocalDateTime.now();
}
