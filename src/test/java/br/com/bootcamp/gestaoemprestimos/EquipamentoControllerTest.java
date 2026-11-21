package br.com.bootcamp.gestaoemprestimos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(roles = "ADMIN")
public class EquipamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCadastrarEquipamentoComSucesso() throws Exception {
        String json = """
        {
            "nome": "Teclado Logitech",
            "patrimonio": "TEC-001",
            "tipo": "TECLADO"
        }
        """;

        mockMvc.perform(post("/equipamentos").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
    }

    @Test
    void deveRetornar404AoBuscarEquipamentoInexistente() throws Exception {

        mockMvc.perform(get("/equipamentos/999999")).andExpect(status().isNotFound());
    }


}
