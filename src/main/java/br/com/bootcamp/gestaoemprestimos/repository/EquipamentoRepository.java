package br.com.bootcamp.gestaoemprestimos.repository;

import br.com.bootcamp.gestaoemprestimos.model.Equipamento;
import br.com.bootcamp.gestaoemprestimos.model.TipoEquipamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    boolean existsByPatrimonio(String patrimonio);
    List<Equipamento> findByTipo(TipoEquipamento tipo);

    List<Equipamento> findByDisponivel(boolean disponivel);

    List<Equipamento> findByTipoAndDisponivel(TipoEquipamento tipo, boolean disponivel);
}
