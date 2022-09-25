package api.controller;

import api.ApplicationTests;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartaoControllerTest extends ApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private CartaoController cartaoController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartaoController).build();
    }

    @AfterAll
    public void tearDown() throws Exception {
        this.testDELETECard();
    }

    @Test
    @DisplayName("Cria o Cartão")
    public void testPOSTCard() throws Exception {
        String data = "{\"numeroCartao\": \"11111111111\", \"senha\": \"222222222\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Pega todos os Cartões")
    public void testGETCards() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cartoes"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Pega o Cartão por Número do Cartão")
    public void testGETCard() throws Exception {
        String data = "{\"numeroCartao\": \"11111111111\", \"senha\": \"222222222\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/cartoes/{numeroCartao}", "11111111111"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Atualiza o Cartão por ID")
    public void testPUTCard() throws Exception {
        String data = "{\"numeroCartao\": \"11111111111\", \"senha\": \"222222222\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String updated = "{\"numeroCartao\": \"3333333333\", \"senha\": \"444444444444\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.put("/cartoes/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updated)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Exclui o Cartão por ID")
    public void testDELETECard() throws Exception {
        String data = "{\"numeroCartao\": \"11111111111\", \"senha\": \"222222222\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/cartoes/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
