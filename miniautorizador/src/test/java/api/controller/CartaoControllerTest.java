package api.controller;

import api.ApplicationTests;
import api.entity.CartaoEntity;
import api.exception.ModelExceptionHandler;
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartaoController)
                .setControllerAdvice(new ModelExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Cria o Cartão com sucesso - 201 com body { senha, numeroCartao }")
    public void testPOSTCard() throws Exception {

        String data = "{\"numeroCartao\": \"1010101010101010\", \"senha\": \"1234\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numeroCartao").value("1010101010101010"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.senha").value("1234"));
    }

    @Test
    @DisplayName("Retorna 422 com body { senha, numeroCartao } ao criar cartão já existente")
    public void testPOSTCardDuplicado() throws Exception {
        String data = "{\"numeroCartao\": \"9090909090909090\", \"senha\": \"1234\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numeroCartao").value("9090909090909090"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.senha").value("1234"));
    }

    @Test
    @DisplayName("Pega o saldo do Cartão por Número do Cartão (string)")
    public void testGETNumberCard() throws Exception {
        String data = "{\"numeroCartao\": \"3030303030303030\", \"senha\": \"5555\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API + "/{numeroCartao}", "3030303030303030"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Retorna 404 ao buscar cartão inexistente")
    public void testGETCartaoInexistente() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API + "/{numeroCartao}", "0000000000000000"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Pega todos os Cartões")
    public void testGETCards() throws Exception {
        String data = "{\"numeroCartao\": \"2020202020202020\", \"senha\": \"3333\"}";

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
    @DisplayName("Pega o Cartão por ID do Cartão")
    public void testGETIdCard() throws Exception {
        String data = "{\"numeroCartao\": \"4040404040404040\", \"senha\": \"6666\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        CartaoEntity lastCard = cartaoRepository.findTopByOrderByIdDesc();

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API + "/id/{id}", lastCard.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Pega os Cartões por Status")
    public void testGETCardsByStatus() throws Exception {
        String data1 = "{\"numeroCartao\": \"6060606060606060\", \"senha\": \"1111\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API + "/status/{status}", "ATIVO"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Exclui o Cartão por ID")
    public void testDELETECard() throws Exception {
        String data = "{\"numeroCartao\": \"8080808080808080\", \"senha\": \"9999\"}";

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
