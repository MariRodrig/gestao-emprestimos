package br.com.bootcamp.gestaoemprestimos;

import br.com.bootcamp.gestaoemprestimos.model.Colaborador;
import br.com.bootcamp.gestaoemprestimos.model.Emprestimo;
import br.com.bootcamp.gestaoemprestimos.model.Equipamento;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EmprestimoTest {


    @Test
    void deveDefinirDataPrevistaPara14DiasAposEmprestimo() {

        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();
        LocalDate dataEmprestimo = LocalDate.of(2026, 11, 14);

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, dataEmprestimo);

        assertEquals(dataEmprestimo, emprestimo.getDataEmprestimo());
        assertEquals(LocalDate.of(2026, 11, 28), emprestimo.getDataPrevistaDevolucao());
    }

    @Test
    void deveRegistrarDataAoDevolverEmprestimo() {

        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();
        LocalDate dataEmprestimo = LocalDate.of(2026, 11, 14);
        LocalDate dataDevolucao = LocalDate.of(2026, 11, 20);

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, dataEmprestimo);

        emprestimo.devolver(dataDevolucao);

        assertEquals(dataDevolucao, emprestimo.getDataDevolucao());
    }

    @Test
    void deveLancarExcecaoAoDevolverDuasVezes() {

        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();
        LocalDate dataEmprestimo = LocalDate.of(2026, 11, 14);
        LocalDate dataDevolucao = LocalDate.of(2026, 11, 20);

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, dataEmprestimo);

        emprestimo.devolver(dataDevolucao);

        assertThrows(IllegalStateException.class, () -> emprestimo.devolver(dataDevolucao));
    }

    @Test
    void deveFicarAtrasadoSeNaoDevolvidoNoPrazoEstipulado() {

        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();
        LocalDate dataEmprestimo = LocalDate.of(2026, 10, 27);

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, dataEmprestimo);

        LocalDate dataConsulta = LocalDate.of(2026, 11, 14);

        assertTrue(emprestimo.estaAtrasadoEm(dataConsulta));
    }

    @Test
    void deveFicarEmDiaSeDentroDoPrazoEstipulado() {

        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();
        LocalDate dataEmprestimo = LocalDate.of(2026, 11, 6);

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, dataEmprestimo);

        LocalDate dataConsulta = LocalDate.of(2026, 11, 14);

        assertFalse(emprestimo.estaAtrasadoEm(dataConsulta));
    }

    @Test
    void deveFicarEmDiaSeDevolvidoDentroDoPrazoEstipulado() {

        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();
        LocalDate dataEmprestimo = LocalDate.of(2026, 10, 27);

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, dataEmprestimo);

        LocalDate dataDevolucao = LocalDate.of(2026, 11, 12);
        LocalDate dataConsulta = LocalDate.of(2026, 11, 14);

        emprestimo.devolver(dataDevolucao);
        assertFalse(emprestimo.estaAtrasadoEm(dataConsulta));
    }

    @Test
    void naoDeveConsiderarAtrasadoNoDiaDoVencimento(){

        Equipamento equipamento = new Equipamento();
        Colaborador colaborador = new Colaborador();

        LocalDate dataEmprestimo = LocalDate.of(2026, 10, 31);
        LocalDate dataConsulta = LocalDate.of(2026, 11, 14);

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, dataEmprestimo);

        assertFalse(emprestimo.estaAtrasadoEm(dataConsulta));
    }
}
