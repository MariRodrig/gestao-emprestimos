package br.com.bootcamp.gestaoemprestimos;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${APP_COLAB_EMAIL}")
    private String emailColaborador;

    @Value("${APP_COLAB_PASSWORD}")
    private String senhaColaborador;

    @Value("${APP_LOGIN_EMAIL}")
    private String emailAdmin;

    @Value("${APP_LOGIN_PASSWORD}")
    private String senhaAdmin;

    @Test
    void deveBloquearAcessoSemToken() throws Exception {

        mockMvc.perform(get("/equipamentos")).andExpect(status().isUnauthorized());
    }

    @Test
    void deveBloquearCadastroParaColaborador() throws Exception {

        String login = """
            {
                "email": "%s",
                "senha": "%s"
            }
            """.formatted(emailColaborador, senhaColaborador);

        String resposta = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(login))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(resposta).get("token").asText();

        String equipamento = """
            {
                "nome": "Teclado Teste",
                "patrimonio": "TEC-SEG-002",
                "tipo": "TECLADO"
            }
            """;

        mockMvc.perform(post("/equipamentos").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(equipamento)).andExpect(status().isForbidden());
    }

    @Test
    void devePermitirCadastroParaAdmin() throws Exception {

        String login = """
            {
                "email": "%s",
                "senha": "%s"
            }
            """.formatted(emailAdmin, senhaAdmin);

        String resposta = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(login)).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        String token = objectMapper.readTree(resposta).get("token").asText();

        String equipamento = """
            {
                "nome": "Mouse Teste",
                "patrimonio": "MOU-SEG-002",
                "tipo": "MOUSE"
            }
            """;

        mockMvc.perform(post("/equipamentos").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(equipamento)).andExpect(status().isCreated());
    }
}
