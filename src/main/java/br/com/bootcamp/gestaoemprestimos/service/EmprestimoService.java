package br.com.bootcamp.gestaoemprestimos.service;

import br.com.bootcamp.gestaoemprestimos.exception.ConflictException;
import br.com.bootcamp.gestaoemprestimos.exception.NotFoundException;
import br.com.bootcamp.gestaoemprestimos.model.Colaborador;
import br.com.bootcamp.gestaoemprestimos.model.Emprestimo;
import br.com.bootcamp.gestaoemprestimos.model.Equipamento;
import br.com.bootcamp.gestaoemprestimos.repository.ColaboradorRepository;
import br.com.bootcamp.gestaoemprestimos.repository.EmprestimoRepository;
import br.com.bootcamp.gestaoemprestimos.repository.EquipamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final ColaboradorRepository colaboradorRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, EquipamentoRepository equipamentoRepository, ColaboradorRepository colaboradorRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    public List<Emprestimo> listar(Boolean atrasados) {
        if (atrasados == null) {
            return emprestimoRepository.findAll();
        }

        return emprestimoRepository.findAll().stream()
                .filter(emprestimo -> emprestimo.isAtrasado() == atrasados).toList();
    }

    public Emprestimo buscar(Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado"));
    }

    public List<Emprestimo> listarDoColaborador(Long colaboradorId) {
        return emprestimoRepository.findByColaborador_Id(colaboradorId);
    }


    @Transactional
    public Emprestimo emprestar(Long equipamentoId, Long colaboradorId) {

        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new NotFoundException("Equipamento não encontrado"));

        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new NotFoundException("Colaborador não encontrado"));

        LocalDate hoje = LocalDate.now();

        boolean possuiAtraso = emprestimoRepository
                .existsByColaborador_IdAndDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(colaborador.getId(), hoje);

        if (possuiAtraso) {
            throw new ConflictException("Colaborador possui empréstimos atrasados");
        }

        try {
            equipamento.emprestar();

        } catch (IllegalStateException e) {
            throw new ConflictException(e.getMessage());
        }

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, hoje);

        equipamentoRepository.save(equipamento);

        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public Emprestimo devolver(Long id) {

        Emprestimo emprestimo = buscar(id);

        Equipamento equipamento = emprestimo.getEquipamento();

        try {
            emprestimo.devolver(LocalDate.now());

        } catch (IllegalStateException e) {
            throw new ConflictException(e.getMessage());
        }

        equipamento.devolver();

        equipamentoRepository.save(equipamento);

        return emprestimoRepository.save(emprestimo);
    }
}