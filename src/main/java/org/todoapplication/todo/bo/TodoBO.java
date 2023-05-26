package org.todoapplication.todo.bo;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.todoapplication.todo.dto.TodoDTO;
import org.todoapplication.todo.entity.Todo;
import org.todoapplication.todo.entity.Usuario;
import org.todoapplication.todo.exception.TaskNotFoundException;
import org.todoapplication.todo.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.todoapplication.todo.constants.TodoConstants.TASK_NOT_FOUND_ERROR;

@Component
public class TodoBO {
    private final TodoRepository todoRepository;
    private final ModelMapper mapper;

    public TodoBO(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
        this.mapper = new ModelMapper();
    }

    public Todo salvar(TodoDTO todoDTO) {
        Todo todoEntity = mapper.map(todoDTO, Todo.class);
        todoEntity.setCriado(LocalDateTime.now());
        todoEntity.setAtualizado(LocalDateTime.now());
        todoEntity.setIdUsuario(getUsuarioAutenticado());

        return todoRepository.save(todoEntity);
    }

    public void atualizar(TodoDTO todoDTO) throws TaskNotFoundException {
        try {
            if(validarUsuario(todoDTO.getId())) {
                todoRepository.atualizar(todoDTO.getId(), todoDTO.getDescricao(), todoDTO.getPrioridade());
            }
            throw new TaskNotFoundException(TASK_NOT_FOUND_ERROR);
        } catch (Exception e) {
            throw new TaskNotFoundException(TASK_NOT_FOUND_ERROR);
        }
    }

    public void deletarTarefa(Long id) throws TaskNotFoundException {
        try {
            if(validarUsuario(id)) {
                todoRepository.deletar(id);
            }
            throw new TaskNotFoundException(TASK_NOT_FOUND_ERROR);
        } catch (Exception e) { // TODO: Lançar exception genérica
            throw new TaskNotFoundException(TASK_NOT_FOUND_ERROR);
        }
    }

    public void concluirTarefa(Long id, boolean status) throws TaskNotFoundException {
        try {
            if(validarUsuario(id)) {
                todoRepository.alterarStatus(id, status);
            }
            throw new TaskNotFoundException(TASK_NOT_FOUND_ERROR);
        } catch (Exception e) {
            throw new TaskNotFoundException(TASK_NOT_FOUND_ERROR);
        }
    }

    public List<Todo> listarTarefasNaoConcluidasPorPrioridade() {
        List<Todo> tarefas = todoRepository.findByConcluidoFalseOrderByPrioridade(getUsuarioAutenticado());
        return ResponseEntity.ok(tarefas).getBody();
    }

    public List<Todo> listarTarefasNaoConcluidasPrioridadeParam(String prioridade) {
        return todoRepository.findByConcluidoFalseOrderByPrioridadeParam(prioridade, getUsuarioAutenticado());
    }

    static Long getUsuarioAutenticado() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        return usuario.getId();
    }

    Boolean validarUsuario(Long id) {
        Optional<Todo> todoEntity = todoRepository.buscarPorId(id);
        Long idUsuario = getUsuarioAutenticado();
        if(todoEntity.isPresent() && idUsuario.equals(todoEntity.get().getIdUsuario())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
