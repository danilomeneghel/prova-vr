package api.service;

import api.ApplicationTests;
import api.entity.CartaoEntity;
import api.entity.SaldoEntity;
import api.entity.TransacaoEntity;
import api.enums.CartaoStatus;
import api.exception.TransacaoException;
import api.model.CriaTransacaoModel;
import api.model.TransacaoModel;
import api.repository.CartaoRepository;
import api.repository.SaldoRepository;
import api.repository.TransacaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransacaoServiceTest extends ApplicationTests {

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private CartaoService cartaoService;

    @MockBean
    private TransacaoRepository transacaoRepository;

    @MockBean
    private CartaoRepository cartaoRepository;

    @MockBean
    private SaldoRepository saldoRepository;

    private SaldoEntity mockSaldoEntity = SaldoEntity.builder()
            .id(1L)
            .valor(BigDecimal.valueOf(500))
            .build();

    @BeforeEach
    public void setUp() {
        when(saldoRepository.findById(1L)).thenReturn(Optional.of(mockSaldoEntity));
    }

    private ModelMapper mapper = new ModelMapper();

    @Test
    @DisplayName("Cria a transação com sucesso usando campo senhaCartao")
    void testSaveTransacao() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("6549873025634501")
                .senha("1234")
                .saldo(mockSaldoEntity)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findByNumeroCartao("6549873025634501")).thenReturn(Optional.of(mockCartaoEntity));

        CriaTransacaoModel mockTransacaoModel = CriaTransacaoModel.builder()
                .numeroCartao("6549873025634501")
                .senhaCartao("1234")
                .valor(BigDecimal.valueOf(10.00))
                .build();

        when(transacaoRepository.save(any(TransacaoEntity.class))).thenReturn(new TransacaoEntity());

        String salvaTransacao = transacaoService.save(mockTransacaoModel);

        Assertions.assertNotNull(salvaTransacao);
        assertEquals("OK", salvaTransacao);
    }

    @Test
    @DisplayName("Lança TransacaoException SALDO_INSUFICIENTE quando saldo for insuficiente")
    void testSaveTransacaoSaldoInsuficiente() {
        SaldoEntity saldoBaixo = SaldoEntity.builder().id(2L).valor(BigDecimal.valueOf(5)).build();

        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("6549873025634501")
                .senha("1234")
                .saldo(saldoBaixo)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findByNumeroCartao("6549873025634501")).thenReturn(Optional.of(mockCartaoEntity));

        CriaTransacaoModel mockTransacaoModel = CriaTransacaoModel.builder()
                .numeroCartao("6549873025634501")
                .senhaCartao("1234")
                .valor(BigDecimal.valueOf(100.00))
                .build();

        TransacaoException ex = assertThrows(TransacaoException.class,
                () -> transacaoService.save(mockTransacaoModel));
        assertEquals("SALDO_INSUFICIENTE", ex.getMessage());
    }

    @Test
    @DisplayName("Lança TransacaoException SENHA_INVALIDA quando senha estiver errada")
    void testSaveTransacaoSenhaInvalida() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("6549873025634501")
                .senha("1234")
                .saldo(mockSaldoEntity)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findByNumeroCartao("6549873025634501")).thenReturn(Optional.of(mockCartaoEntity));

        CriaTransacaoModel mockTransacaoModel = CriaTransacaoModel.builder()
                .numeroCartao("6549873025634501")
                .senhaCartao("ERRADA")
                .valor(BigDecimal.valueOf(10.00))
                .build();

        TransacaoException ex = assertThrows(TransacaoException.class,
                () -> transacaoService.save(mockTransacaoModel));
        assertEquals("SENHA_INVALIDA", ex.getMessage());
    }

    @Test
    @DisplayName("Lança TransacaoException CARTAO_INEXISTENTE quando cartão não existir")
    void testSaveTransacaoCartaoInexistente() {
        when(cartaoRepository.findByNumeroCartao("0000000000000000")).thenReturn(Optional.empty());

        CriaTransacaoModel mockTransacaoModel = CriaTransacaoModel.builder()
                .numeroCartao("0000000000000000")
                .senhaCartao("1234")
                .valor(BigDecimal.valueOf(10.00))
                .build();

        TransacaoException ex = assertThrows(TransacaoException.class,
                () -> transacaoService.save(mockTransacaoModel));
        assertEquals("CARTAO_INEXISTENTE", ex.getMessage());
    }

    @Test
    @DisplayName("Localiza todas as transações com sucesso")
    void testFindAll() {
        SaldoEntity saldo = new SaldoEntity();
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("2202202202202202")
                .saldo(saldo)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findById(1L)).thenReturn(Optional.of(mockCartaoEntity));

        List<TransacaoEntity> mockTransacaoEntities = Stream.of(
                        TransacaoEntity.builder()
                                .id(1L)
                                .cartao(mockCartaoEntity)
                                .valor(BigDecimal.valueOf(10.50))
                                .build(),
                        TransacaoEntity.builder()
                                .id(2L)
                                .cartao(mockCartaoEntity)
                                .valor(BigDecimal.valueOf(11.55))
                                .build())
                .collect(Collectors.toList());

        when(transacaoRepository.findAll()).thenReturn(mockTransacaoEntities);

        List<TransacaoModel> transacoes = transacaoService.findAll();
        List<TransacaoEntity> listaTransacoes = transacoes.stream()
                .map(entity -> mapper.map(entity, TransacaoEntity.class))
                .collect(Collectors.toList());

        Assertions.assertNotNull(listaTransacoes);
        assertEquals(mockTransacaoEntities.size(), listaTransacoes.size());
    }

    @Test
    @DisplayName("Localiza a transação por ID com sucesso")
    void testFindTransacaoById() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("2202202202202202")
                .saldo(mockSaldoEntity)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findById(1L)).thenReturn(Optional.of(mockCartaoEntity));

        TransacaoEntity mockTransacaoEntity = TransacaoEntity.builder()
                .id(1L)
                .cartao(mockCartaoEntity)
                .valor(BigDecimal.valueOf(10.50))
                .build();

        when(transacaoRepository.findById(1L)).thenReturn(Optional.of(mockTransacaoEntity));

        TransacaoModel findTransacao = transacaoService.findById(1L);

        Assertions.assertNotNull(findTransacao);
        assertEquals(mockTransacaoEntity.getId(), findTransacao.getId());
        assertEquals(mockTransacaoEntity.getValor(), findTransacao.getValor());
    }

    @Test
    @DisplayName("Exclui a transação por ID com sucesso")
    void testDeleteTransacaoById() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("4404404404404404")
                .senha("1234")
                .saldo(mockSaldoEntity)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findByNumeroCartao("4404404404404404")).thenReturn(Optional.of(mockCartaoEntity));

        TransacaoEntity mockTransacaoEntity = TransacaoEntity.builder()
                .id(1L)
                .cartao(mockCartaoEntity)
                .valor(BigDecimal.valueOf(10.50))
                .build();

        when(transacaoRepository.findById(1L)).thenReturn(Optional.of(mockTransacaoEntity));

        String deleteTransacao = transacaoService.deleteById(1L);

        Assertions.assertNotNull(deleteTransacao);
        assertEquals("Transação excluída com sucesso.", deleteTransacao);
    }

}
