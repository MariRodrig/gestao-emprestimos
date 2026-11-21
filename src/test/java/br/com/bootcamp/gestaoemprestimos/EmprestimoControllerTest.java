package br.com.bootcamp.gestaoemprestimos;


import br.com.bootcamp.gestaoemprestimos.model.Colaborador;
import br.com.bootcamp.gestaoemprestimos.model.Emprestimo;
import br.com.bootcamp.gestaoemprestimos.model.Equipamento;
import br.com.bootcamp.gestaoemprestimos.model.TipoEquipamento;
import br.com.bootcamp.gestaoemprestimos.repository.ColaboradorRepository;
import br.com.bootcamp.gestaoemprestimos.repository.EmprestimoRepository;
import br.com.bootcamp.gestaoemprestimos.repository.EquipamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(roles = "ADMIN")
public class EmprestimoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    private Equipamento equipamento;
    private Colaborador colaborador;

    @BeforeEach
    void prepararDados() {

        equipamento = new Equipamento();
        equipamento.setNome("Notebook Teste");
        equipamento.setPatrimonio("NOTE-TESTE");
        equipamento.setTipo(TipoEquipamento.NOTEBOOK);

        equipamento = equipamentoRepository.save(equipamento);

        colaborador = new Colaborador();
        colaborador.setNome("Colaborador Teste");
        colaborador.setEmail("colaborador.teste@example.com");

        colaborador = colaboradorRepository.save(colaborador);
    }

    @Test
    void deveEmprestarEquipamentoDisponivel() throws Exception {

        String json = """
                {
                    "equipamentoId": %d,
                    "colaboradorId": %d
                }
                """.formatted(equipamento.getId(), colaborador.getId());

        mockMvc.perform(post("/emprestimos").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.equipamentoId").value(equipamento.getId()))
                .andExpect(jsonPath("$.colaboradorId").value(colaborador.getId()));
    }

    @Test
    void deveDevolverEquipamento() throws Exception {

        equipamento.emprestar();
        equipamentoRepository.save(equipamento);

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, LocalDate.now());

        emprestimo = emprestimoRepository.save(emprestimo);

        mockMvc.perform(put("/emprestimos/{id}/devolucao", emprestimo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataDevolucao").isNotEmpty());
    }

    @Test
    void deveImpedirEmprestimoDeEquipamentoOcupado() throws Exception {

        equipamento.emprestar();
        equipamentoRepository.save(equipamento);

        String json = """
                {
                    "equipamentoId": %d,
                    "colaboradorId": %d
                }
                """.formatted(equipamento.getId(), colaborador.getId());

        mockMvc.perform(post("/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void deveImpedirEmprestimoParaColaboradorAtrasado() throws Exception {

        LocalDate dataAntiga = LocalDate.now().minusDays(20);

        Emprestimo emprestimoAtrasado = new Emprestimo(equipamento, colaborador, dataAntiga);

        emprestimoRepository.save(emprestimoAtrasado);

        String json = """
                {
                    "equipamentoId": %d,
                    "colaboradorId": %d
                }
                """.formatted(equipamento.getId(), colaborador.getId());

        mockMvc.perform(post("/emprestimos").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("Colaborador possui empréstimos atrasados"));

    }

    @Test
    void deveImpedirDevolucaoDuplicada() throws Exception {

        equipamento.emprestar();
        equipamentoRepository.save(equipamento);

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, LocalDate.now());

        emprestimo = emprestimoRepository.save(emprestimo);

        mockMvc.perform(put("/emprestimos/{id}/devolucao", emprestimo.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(put("/emprestimos/{id}/devolucao", emprestimo.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem")
                        .value("Empréstimo já devolvido"));
    }

    @Test
    void deveImpedirExclusaoEquipamentoComHistorico() throws Exception {

        Emprestimo emprestimo = new Emprestimo(equipamento, colaborador, LocalDate.now());

        emprestimoRepository.save(emprestimo);

        mockMvc.perform(delete("/equipamentos/{id}", equipamento.getId())).andExpect(status().isConflict());
    }
}