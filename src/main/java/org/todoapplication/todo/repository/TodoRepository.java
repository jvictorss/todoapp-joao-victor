package org.todoapplication.todo.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.todoapplication.todo.entity.Todo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends CrudRepository<Todo, Long> {
    @Query("SELECT t FROM Todo t WHERE t.id = ?1")
    Optional<Todo> buscarPorId(Long id);

    @Modifying
    @Query("DELETE FROM Todo t WHERE t.id = ?1")
    void excluirTarefa(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Todo t set t.descricao = ?2, t.prioridade = ?3 WHERE t.id = ?1")
    void atualizar(Long id, String descricao, String prioridade);

    @Modifying
    @Transactional
    @Query("UPDATE Todo t set t.concluido = ?2 WHERE t.id = ?1")
    void alterarStatus(Long id, Boolean status);

    @Query("SELECT t FROM Todo t WHERE t.idUsuario=?1 AND t.concluido = false ORDER BY CASE t.prioridade " +
            "WHEN 'Alta' THEN 0 " +
            "WHEN 'Média' THEN 1 " +
            "ELSE 2 " +
            "END")
    List<Todo> findByConcluidoFalseOrderByPrioridade(Long usuarioAutenticado);

    @Query("SELECT t FROM Todo t WHERE t.concluido = false and t.prioridade =?1 and t.idUsuario=?2")
    List<Todo> findByConcluidoFalseOrderByPrioridadeParam(String prioridade, Long usuarioAutenticado);

    @Modifying
    @Query("DELETE FROM Todo t WHERE t.id=?1")
    void deletar(Long id);
}