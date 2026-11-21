package br.com.bootcamp.gestaoemprestimos.controller;

import br.com.bootcamp.gestaoemprestimos.model.Equipamento;
import br.com.bootcamp.gestaoemprestimos.model.TipoEquipamento;
import br.com.bootcamp.gestaoemprestimos.service.EquipamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Equipamentos", description = "Cadastro e gerenciamento de equipamentos")
@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoService equipamentoService) {
        this.equipamentoService = equipamentoService;
    }

    @GetMapping
    public List<Equipamento> listar(@RequestParam(required = false) TipoEquipamento tipoEquipamento, @RequestParam(required = false) Boolean disponivel) {
        return equipamentoService.listar(tipoEquipamento, disponivel);
    }

    @GetMapping("/{id}")
    public Equipamento buscar(@PathVariable Long id) {
        return equipamentoService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Equipamento cadastrar(@RequestBody Equipamento equipamento) {
        return equipamentoService.cadastrar(equipamento);
    }

    @PutMapping("/{id}")
    public Equipamento atualizar(@PathVariable Long id, @RequestBody Equipamento equipamento) {
        return equipamentoService.atualizar(id, equipamento);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        equipamentoService.remover(id);
    }
}
