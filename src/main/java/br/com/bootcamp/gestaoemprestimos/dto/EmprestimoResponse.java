package br.com.bootcamp.gestaoemprestimos.dto;

import br.com.bootcamp.gestaoemprestimos.model.Emprestimo;

import java.time.LocalDate;

public record EmprestimoResponse(Long id,
                                 Long equipamentoId,
                                 Long colaboradorId,
                                 LocalDate dataEmprestimo,
                                 LocalDate dataPrevistaDevolucao,
                                 LocalDate dataDevolucao,
                                 boolean atrasado) {


    public static EmprestimoResponse from(Emprestimo emprestimo) {
        return new EmprestimoResponse(
                emprestimo.getId(),
                emprestimo.getEquipamento().getId(),
                emprestimo.getColaborador().getId(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataPrevistaDevolucao(),
                emprestimo.getDataDevolucao(),
                emprestimo.isAtrasado()
        );
    }
}
