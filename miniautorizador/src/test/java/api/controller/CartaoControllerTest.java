package api.controller;

import api.ApplicationTests;
import api.entity.CartaoEntity;
import api.repository.CartaoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartaoControllerTest extends ApplicationTests {

    private static final String URL_API = "/cartoes";

    private MockMvc mockMvc;

    @Autowired
    private CartaoController cartaoController;

    @Autowired
    private CartaoRepository cartaoRepository;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartaoController).build();
    }

    @Test
    @DisplayName("Cria o Cartão")
    public void testPOSTCard() throws Exception {
        String data = "{\"numeroCartao\": \"1010101010101010\", \"senha\": \"222222222\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Pega todos os Cartões")
    public void testGETCards() throws Exception {
        String data = "{\"numeroCartao\": \"2020202020202020\", \"senha\": \"33333333333\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Pega o Cartão por Número do Cartão")
    public void testGETNumberCard() throws Exception {
        String data = "{\"numeroCartao\": \"3030303030303030\", \"senha\": \"55555555555\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API+"/{numeroCartao}", "3030303030303030"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Pega o Cartão por ID do Cartão")
    public void testGETIdCard() throws Exception {
        String data = "{\"numeroCartao\": \"4040404040404040\", \"senha\": \"6666666666666\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        CartaoEntity lastCard = cartaoRepository.findTopByOrderByIdDesc();

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API+"/id/{id}", lastCard.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Atualiza o Cartão por ID")
    public void testPUTCard() throws Exception {
        String data = "{\"numeroCartao\": \"5050505050505050\", \"senha\": \"77777777777\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String updated = "{\"numeroCartao\": \"7777777777777777\", \"senha\": \"88888888888\", \"status\": \"ATIVO\"}";

        CartaoEntity lastCard = cartaoRepository.findTopByOrderByIdDesc();

        this.mockMvc.perform(MockMvcRequestBuilders.put(URL_API+"/{id}", lastCard.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updated)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Pega os Cartões por Status")
    public void testGETCardsByStatus() throws Exception {
        String data1 = "{\"numeroCartao\": \"6060606060606060\", \"senha\": \"1111111111\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String data2 = "{\"numeroCartao\": \"7070707070707070\", \"senha\": \"2222222222\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API + "/status/{status}", "ATIVO"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Exclui o Cartão por ID")
    public void testDELETECard() throws Exception {
        String data = "{\"numeroCartao\": \"8080808080808080\", \"senha\": \"9999999999999\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        CartaoEntity lastCard = cartaoRepository.findTopByOrderByIdDesc();

        if (lastCard != null) {
            this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_API + "/{id}", lastCard.getId()))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } else {
            throw new RuntimeException("Nenhum cartão encontrado.");
        }
    }

}
