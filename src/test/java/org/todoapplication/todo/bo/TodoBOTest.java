package org.todoapplication.todo.bo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.todoapplication.todo.dto.TodoDTO;
import org.todoapplication.todo.entity.Todo;
import org.todoapplication.todo.entity.Usuario;
import org.todoapplication.todo.exception.TaskNotFoundException;
import org.todoapplication.todo.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoBOTest {
    @Mock
    private TodoRepository todoRepository;

    @Mock
    private Usuario usuario;
    @InjectMocks
    private TodoBO todoBO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Authentication authentication = mock(Authentication.class);
        Usuario usuarioAutenticado = new Usuario();
        usuarioAutenticado.setId(1L);

        when(authentication.getPrincipal()).thenReturn(usuarioAutenticado);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void salvar_SetsCriadoAtualizadoIdUsuarioAndCallsSave() {
        TodoDTO todoDTO = new TodoDTO();
        when(todoRepository.save(any(Todo.class))).thenReturn(new Todo());

        Todo result = todoBO.salvar(todoDTO);

        verify(todoRepository, times(1)).save(any(Todo.class));
        Assertions.assertNotNull(result.getCriado());
        Assertions.assertNotNull(result.getAtualizado());
        Assertions.assertEquals(result.getIdUsuario(), null);
    }

    @Test
    void atualizar_ValidatesUserAndCallsAtualizar() throws TaskNotFoundException {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setId(1L);
        todoDTO.setDescricao("Descrição do Todo");
        todoDTO.setPrioridade("Alta");

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setDescricao("Descrição do Todo");
        todo.setPrioridade("Alta");
        todo.setIdUsuario(1L);

        when(todoRepository.buscarPorId(1L)).thenReturn(Optional.of(todo));
        when(todoBO.validarUsuario(Mockito.anyLong())).thenReturn(true);
        doNothing().when(todoRepository).atualizar(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());

        assertEquals(todoDTO.getId(), todo.getId());
        assertEquals(todoDTO.getDescricao(), todo.getDescricao());
        assertEquals(todoDTO.getPrioridade(), todo.getPrioridade());
    }

    @Test
    void atualizar_InvalidTodoDTO_ThrowsTaskNotFoundException() {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setId(1L);

        when(todoRepository.buscarPorId(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> todoBO.atualizar(todoDTO));

        verify(todoRepository, never()).atualizar(anyLong(), anyString(), anyString());
    }

    @Test
    void atualizar_ThrowsTaskNotFoundException_WhenUserValidationFails() throws TaskNotFoundException {
        TodoDTO todoDTO = new TodoDTO();
        Long id = 1L;
        when(todoRepository.buscarPorId(Mockito.anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(TaskNotFoundException.class, () -> {
            todoBO.atualizar(todoDTO);
        });
    }

    @Test
    void deletarTarefa_ThrowsTaskNotFoundException_WhenUserValidationFails() {
        // Arrange
        Long id = 1L;
        when(todoRepository.buscarPorId(Mockito.anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(TaskNotFoundException.class, () -> {
            todoBO.deletarTarefa(id);
        });
    }

    @Test
    void concluirTarefa_ThrowsTaskNotFoundException_WhenUserValidationFails() throws TaskNotFoundException {
        Long id = 1L;
        boolean status = true;
        when(todoBO.validarUsuario(id)).thenReturn(Boolean.FALSE);
        Assertions.assertThrows(TaskNotFoundException.class, () -> {
            todoBO.concluirTarefa(id, status);
        });
    }

    @Test
    void listarTarefasNaoConcluidasPorPrioridade_ReturnsListOfTasks() {
        // Arrange
        List<Todo> expectedTasks = new ArrayList<>();
        when(todoRepository.findByConcluidoFalseOrderByPrioridade(todoBO.getUsuarioAutenticado())).thenReturn(expectedTasks);

        // Act
        List<Todo> result = todoBO.listarTarefasNaoConcluidasPorPrioridade();

        // Assert
        verify(todoRepository, times(1)).findByConcluidoFalseOrderByPrioridade(todoBO.getUsuarioAutenticado());
        Assertions.assertEquals(expectedTasks, result);
    }

    @Test
    void listarTarefasNaoConcluidasPrioridadeParam_ReturnsListOfTasks() {
        // Arrange
        List<Todo> expectedTasks = new ArrayList<>();
        String prioridade = "alta";
        when(todoRepository.findByConcluidoFalseOrderByPrioridadeParam(prioridade, TodoBO.getUsuarioAutenticado()))
                .thenReturn(expectedTasks);

        // Act
        List<Todo> result = todoBO.listarTarefasNaoConcluidasPrioridadeParam(prioridade);

        // Assert
        verify(todoRepository, times(1)).findByConcluidoFalseOrderByPrioridadeParam(prioridade,
                todoBO.getUsuarioAutenticado());
        Assertions.assertEquals(expectedTasks, result);
    }

    @Test
    void validarUsuario_ReturnsFalse_WhenTodoDoesNotExist() {
        Long id = 1L;
        when(todoRepository.buscarPorId(id)).thenReturn(Optional.empty());

        Boolean result = todoBO.validarUsuario(id);

        Assertions.assertFalse(result);
    }

    @Test
    void validarUsuario_ReturnsFalse_WhenUserIsNotAuthenticated() {
        Long id = 1L;
        Todo todo = new Todo();
        todo.setId(id);
        when(todoRepository.buscarPorId(id)).thenReturn(Optional.of(todo));

        Boolean result = todoBO.validarUsuario(id);

        Assertions.assertFalse(result);
    }
}
