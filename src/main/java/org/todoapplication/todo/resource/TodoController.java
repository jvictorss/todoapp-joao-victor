package org.todoapplication.todo.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.todoapplication.todo.bo.TodoBO;
import org.todoapplication.todo.dto.TodoDTO;
import org.todoapplication.todo.entity.Todo;
import org.todoapplication.todo.exception.CantSaveException;
import org.todoapplication.todo.exception.TaskNotFoundException;
import org.todoapplication.todo.exception.UnauthorizedException;

import java.util.List;

@Api(value = "Tarefas", description = "Funcionalidade que gerencia as tarefas do usuário", tags = "Tarefas")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request", response = CantSaveException.class),
        @ApiResponse(code = 404, message = "Not found", response = TaskNotFoundException.class)})
@RestController
@RequestMapping(path = "/todo")
public class TodoController {
    private final TodoBO todoBO;

    @Autowired
    public TodoController(TodoBO todoBO) {
        this.todoBO = todoBO;
    }

    @ApiOperation(value = "Salvar uma nova tarefa")
    @PostMapping(path = "/salvar")
    public Todo salvarTarefa(@RequestBody TodoDTO todoDTO) {
        return todoBO.salvar(todoDTO);
    }

    @ApiOperation(value = "Atualizar uma tarefa existente")
    @PostMapping(path = "/atualizar")
    public ResponseEntity<Void> atualizarTarefa(@RequestBody TodoDTO todoDTO) throws Exception {
        todoBO.atualizar(todoDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Deletar uma tarefa")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable("id") Long id) throws Exception {
        todoBO.deletarTarefa(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiOperation(value = "Alterar o status de uma tarefa que não foi concluída.")
    @PostMapping("/alterar-status")
    public ResponseEntity<Void> alterarStatusTarefa(@RequestBody TodoDTO todoDTO)
            throws TaskNotFoundException, UnauthorizedException {
        todoBO.concluirTarefa(todoDTO.getId(), todoDTO.getConcluido());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Listar tarefas pendentes por prioridade")
    @GetMapping("/listar-tarefas/{prioridade}")
    public ResponseEntity<List<Todo>> listarTarefasPendentesPrioridade(@PathVariable String prioridade) {
        List<Todo> lista = todoBO.listarTarefasNaoConcluidasPrioridadeParam(prioridade);

        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @ApiOperation(value = "Listar tarefas pendentes")
    @GetMapping("/listar-tarefas-pendentes")
    public List<Todo> listarTarefasPendentes(@RequestParam(value = "prioridade", required = false, defaultValue = "null")
                                             String prioridade) throws TaskNotFoundException, UnauthorizedException {
        return todoBO.listarTarefasNaoConcluidasPorPrioridade();
    }
}
