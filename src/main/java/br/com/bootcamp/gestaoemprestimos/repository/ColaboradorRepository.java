package br.com.bootcamp.gestaoemprestimos.repository;

import br.com.bootcamp.gestaoemprestimos.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    boolean existsByEmail(String email);
}
