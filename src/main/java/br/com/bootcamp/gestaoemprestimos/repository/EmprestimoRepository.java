package br.com.bootcamp.gestaoemprestimos.repository;

import br.com.bootcamp.gestaoemprestimos.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByColaborador_Id(Long colaboradorId);

    boolean existsByEquipamento_Id(Long equipamentoId);

    List<Emprestimo> findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate data);

    boolean existsByColaborador_IdAndDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(Long colaboradorId, LocalDate data);
}
