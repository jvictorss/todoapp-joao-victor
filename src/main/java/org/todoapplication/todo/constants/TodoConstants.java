package org.todoapplication.todo.constants;

import org.springframework.stereotype.Component;

@Component
public class TodoConstants {
    public static final String INVALID_PASSWORD_EXCEPTION = "Senha não corresponde aos requisitos mínimos de segurança." +
            "Deve conter letras maiúsculas, minúsculas, números e pelo menos 1 caractere especial: !@#$%&* ";
    public static final String TASK_NOT_FOUND_ERROR = "Desculpe. Não foi possível encontrar a tarefa especificada.";
    public static final String ERROR_SAVE_USER = "Desculpe, não foi possível salvar este usuário.";
    public static final String ERROR_CONFLICT_SAVE_USER = "Não foi possível salvar. Já existe um usuário com o e-mail cadastrado.";
    public static final String ERROR_USER_NOT_FOUND = "Não foi possível encontrar o usuário especificado.";
}
