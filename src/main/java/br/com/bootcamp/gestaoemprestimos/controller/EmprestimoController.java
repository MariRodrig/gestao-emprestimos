package br.com.bootcamp.gestaoemprestimos.controller;

import br.com.bootcamp.gestaoemprestimos.dto.EmprestimoRequest;
import br.com.bootcamp.gestaoemprestimos.dto.EmprestimoResponse;
import br.com.bootcamp.gestaoemprestimos.model.Emprestimo;
import br.com.bootcamp.gestaoemprestimos.service.EmprestimoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Empréstimos", description = "Empréstimos e devoluções de equipamentos")
@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    public List<EmprestimoResponse> listar(@RequestParam(required = false) Boolean atrasados) {

        return emprestimoService.listar(atrasados)
                .stream()
                .map(EmprestimoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public EmprestimoResponse buscar(@PathVariable Long id) {
        Emprestimo emprestimo = emprestimoService.buscar(id);
        return EmprestimoResponse.from(emprestimo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmprestimoResponse emprestar(@RequestBody @Valid EmprestimoRequest request) {

        Emprestimo emprestimo = emprestimoService.emprestar(request.equipamentoId(), request.colaboradorId());

        return EmprestimoResponse.from(emprestimo);
    }

    @PutMapping("/{id}/devolucao")
    public EmprestimoResponse devolver(@PathVariable Long id) {

        Emprestimo emprestimo = emprestimoService.devolver(id);

        return EmprestimoResponse.from(emprestimo);
    }
}
