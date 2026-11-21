package br.com.bootcamp.gestaoemprestimos.service;

import br.com.bootcamp.gestaoemprestimos.exception.ConflictException;
import br.com.bootcamp.gestaoemprestimos.exception.NotFoundException;
import br.com.bootcamp.gestaoemprestimos.model.Equipamento;
import br.com.bootcamp.gestaoemprestimos.model.TipoEquipamento;
import br.com.bootcamp.gestaoemprestimos.repository.EmprestimoRepository;
import br.com.bootcamp.gestaoemprestimos.repository.EquipamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final EmprestimoRepository emprestimoRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository, EmprestimoRepository emprestimoRepository) {
        this.equipamentoRepository = equipamentoRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    public List<Equipamento> listar(TipoEquipamento tipoEquipamento, Boolean disponivel) {
        if (tipoEquipamento != null && disponivel != null) {
            return equipamentoRepository.findByTipoAndDisponivel(tipoEquipamento, disponivel);
        }

        if (tipoEquipamento != null) {
            return equipamentoRepository.findByTipo(tipoEquipamento);
        }

        if (disponivel != null) {
            return equipamentoRepository.findByDisponivel(disponivel);
        }

        return equipamentoRepository.findAll();
    }

    public Equipamento buscar(Long id) {
        return equipamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipamento não encontrado"));
    }

    public Equipamento cadastrar(Equipamento equipamento){
        if (equipamentoRepository.existsByPatrimonio(equipamento.getPatrimonio())){
            throw new ConflictException("Patrimônio já cadastrado");
        }
        equipamento.setDisponivel(true);
        return equipamentoRepository.save(equipamento);
    }

    public Equipamento atualizar(Long id, Equipamento equipamento){
        Equipamento existente = buscar(id);

        existente.setNome(equipamento.getNome());
        existente.setTipo(equipamento.getTipo());

        return equipamentoRepository.save(existente);
    }


    public void remover(Long id) {

        Equipamento equipamento = buscar(id);

        if (emprestimoRepository.existsByEquipamento_Id(id)) {
            throw new ConflictException("Equipamento possui histórico de empréstimos");
        }
        equipamentoRepository.delete(equipamento);
    }
}
