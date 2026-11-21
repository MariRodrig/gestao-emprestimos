package br.com.bootcamp.gestaoemprestimos.controller;

import br.com.bootcamp.gestaoemprestimos.dto.ColaboradorRequest;
import br.com.bootcamp.gestaoemprestimos.dto.ColaboradorResponse;
import br.com.bootcamp.gestaoemprestimos.model.Colaborador;
import br.com.bootcamp.gestaoemprestimos.model.Emprestimo;
import br.com.bootcamp.gestaoemprestimos.service.ColaboradorService;
import br.com.bootcamp.gestaoemprestimos.service.EmprestimoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Tag(name = "Colaboradores", description = "Cadastro e consulta de colaboradores")
@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;
    private final EmprestimoService emprestimoService;

    public ColaboradorController(ColaboradorService colaboradorService, EmprestimoService emprestimoService) {
        this.colaboradorService = colaboradorService;
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    public List<ColaboradorResponse> listar() {

        List<Colaborador> colaboradores = colaboradorService.listar();
        return colaboradores.stream()
                .map(colaborador -> new ColaboradorResponse(colaborador.getId(), colaborador.getNome(),
                        colaborador.getEmail(), colaborador.getCargo()))
                .toList();
    }

    @GetMapping("/{id}")
    public ColaboradorResponse buscar(@PathVariable Long id) {

        Colaborador colaborador = colaboradorService.buscar(id);

        return new ColaboradorResponse(colaborador.getId(),
                colaborador.getNome(),
                colaborador.getEmail(),
                colaborador.getCargo());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ColaboradorResponse cadastrar(@Valid @RequestBody ColaboradorRequest request) {

        Colaborador colaborador = colaboradorService.cadastrar(request);

        return new ColaboradorResponse(colaborador.getId(),
                colaborador.getNome(),
                colaborador.getEmail(),
                colaborador.getCargo());
    }

    @GetMapping("/{id}/emprestimos")
    public List<Emprestimo> listarEmprestimos(@PathVariable Long id) {
        return emprestimoService.listarDoColaborador(id);
    }
}
