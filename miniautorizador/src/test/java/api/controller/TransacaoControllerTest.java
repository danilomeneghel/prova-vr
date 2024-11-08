package api.controller;

import api.ApplicationTests;
import api.entity.TransacaoEntity;
import api.repository.TransacaoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransacaoControllerTest extends ApplicationTests {

    private static final String URL_API = "/transacoes";

    private MockMvc mockMvc;

    @Autowired
    private TransacaoController transacaoController;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private CartaoController cartaoController;

    @BeforeEach
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(transacaoController).build();
    }

    @Test
    @DisplayName("Cria a Transação")
    public void testPOSTTransaction() throws Exception {
        String cartao = "{\"numeroCartao\": \"1111111111111111\", \"senha\": \"222222222\", \"status\": \"ATIVO\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(cartaoController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartao)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String transacao = "{\"numeroCartao\": \"1111111111111111\", \"senha\": \"222222222\", \"valor\": \"10.20\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(transacaoController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transacao)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Pega todas as Transações")
    public void testGETTransactions() throws Exception {
        transacaoRepository.deleteAll();

        String cartao = "{\"numeroCartao\": \"2222222222222222\", \"senha\": \"333333333333\", \"status\": \"ATIVO\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(cartaoController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartao)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String transacao = "{\"numeroCartao\": \"2222222222222222\", \"senha\": \"333333333333\", \"valor\": \"10.20\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(transacaoController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transacao)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].valor").value(10.2));
    }

    @Test
    @DisplayName("Pega o Transação por ID do Transação")
    public void testGETTransaction() throws Exception {
        String cartao = "{\"numeroCartao\": \"3333333333333333\", \"senha\": \"444444444444\", \"status\": \"ATIVO\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(cartaoController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartao)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String transacao = "{\"numeroCartao\": \"3333333333333333\", \"senha\": \"444444444444\", \"valor\": \"10.20\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(transacaoController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transacao)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        TransacaoEntity lastTransaction = transacaoRepository.findTopByOrderByIdDesc();

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API+"/{id}", lastTransaction.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Exclui o Transação por ID")
    public void testDELETETransaction() throws Exception {
        String cartao = "{\"numeroCartao\": \"5555555555555555\", \"senha\": \"66666666666\", \"status\": \"ATIVO\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(cartaoController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartao)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String transacao = "{\"numeroCartao\": \"5555555555555555\", \"senha\": \"66666666666\", \"valor\": \"10.20\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(transacaoController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transacao)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        TransacaoEntity lastTransaction = transacaoRepository.findTopByOrderByIdDesc();

        if (lastTransaction != null) {
            this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_API + "/{id}", lastTransaction.getId()))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } else {
            throw new RuntimeException("Nenhuma transação encontrada.");
        }
    }

}
