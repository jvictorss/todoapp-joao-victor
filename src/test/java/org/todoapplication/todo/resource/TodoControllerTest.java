package org.todoapplication.todo.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.todoapplication.todo.bo.TodoBO;
import org.todoapplication.todo.dto.TodoDTO;
import org.todoapplication.todo.entity.Todo;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TodoControllerTest {

    @Mock
    private TodoBO todoBO;

    private TodoController todoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        todoController = new TodoController(todoBO);
    }

    @Test
    void salvarTarefa_WhenValidTodoDTO_ReturnsSavedTodo() {
        TodoDTO todoDTO = new TodoDTO();
        Todo savedTodo = new Todo();
        when(todoBO.salvar(todoDTO)).thenReturn(savedTodo);

        Todo result = todoController.salvarTarefa(todoDTO);

        assertEquals(savedTodo, result);
        verify(todoBO, times(1)).salvar(todoDTO);
    }

    @Test
    void atualizarTarefa_WhenValidTodoDTO_ReturnsNoContentResponse() throws Exception {
        TodoDTO todoDTO = new TodoDTO();

        ResponseEntity<Void> response = todoController.atualizarTarefa(todoDTO);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(todoBO, times(1)).atualizar(todoDTO);
    }

    @Test
    void deletarTarefa_WhenValidId_ReturnsOkResponse() throws Exception {
        Long id = 1L;

        ResponseEntity<Void> response = todoController.deletarTarefa(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(todoBO, times(1)).deletarTarefa(id);
    }

    @Test
    void listarTarefasPendentesPrioridade_WhenValidPrioridade_ReturnsTodoList() {
        String prioridade = "alta";
        List<Todo> todoList = new ArrayList<>();
        when(todoBO.listarTarefasNaoConcluidasPrioridadeParam(prioridade)).thenReturn(todoList);

        ResponseEntity<List<Todo>> response = todoController.listarTarefasPendentesPrioridade(prioridade);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(todoList, response.getBody());
        verify(todoBO, times(1)).listarTarefasNaoConcluidasPrioridadeParam(prioridade);
    }

    @Test
    void listarTarefasPendentes_WhenPrioridadeNotProvided_ReturnsTodoList() throws Exception {
        List<Todo> todoList = new ArrayList<>();
        when(todoBO.listarTarefasNaoConcluidasPorPrioridade()).thenReturn(todoList);

        List<Todo> result = todoController.listarTarefasPendentes(null);

        assertEquals(todoList, result);
        verify(todoBO, times(1)).listarTarefasNaoConcluidasPorPrioridade();
    }

}
