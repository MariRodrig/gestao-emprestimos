package br.com.bootcamp.gestaoemprestimos.service;

import br.com.bootcamp.gestaoemprestimos.dto.ColaboradorRequest;
import br.com.bootcamp.gestaoemprestimos.exception.ConflictException;
import br.com.bootcamp.gestaoemprestimos.exception.NotFoundException;
import br.com.bootcamp.gestaoemprestimos.model.Colaborador;
import br.com.bootcamp.gestaoemprestimos.model.Equipamento;
import br.com.bootcamp.gestaoemprestimos.repository.ColaboradorRepository;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorService(ColaboradorRepository colaboradorRepository) {
        this.colaboradorRepository = colaboradorRepository;
    }

    public List<Colaborador> listar() {
        return colaboradorRepository.findAll();
    }

    public Colaborador buscar(Long id) {
        return colaboradorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Colaborador não encontrado"));
    }

    public Colaborador cadastrar(ColaboradorRequest request) {
        if (colaboradorRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email já cadastrado");
        }

        Colaborador colaborador = new Colaborador();

        colaborador.setNome(request.nome());
        colaborador.setEmail(request.email());
        colaborador.setCargo(request.cargo());

        return colaboradorRepository.save(colaborador);

    }
}