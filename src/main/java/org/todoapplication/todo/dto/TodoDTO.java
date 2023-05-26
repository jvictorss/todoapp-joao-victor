package org.todoapplication.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {

    private Long id;

    private String descricao;

    private String prioridade;

    private Boolean concluido;

    private Long user;
}
