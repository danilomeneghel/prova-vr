package api.service;

import api.ApplicationTests;
import api.entity.CartaoEntity;
import api.entity.SaldoEntity;
import api.entity.TransacaoEntity;
import api.enums.CartaoStatus;
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
    @DisplayName("Cria a Transação com sucesso")
    void testSaveTransacao() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("110110110110110")
                .senha("aaaaaaaaaaa")
                .saldo(mockSaldoEntity)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findByNumeroCartao("110110110110110")).thenReturn(Optional.of(mockCartaoEntity));

        CriaTransacaoModel mockTransacaoModel = CriaTransacaoModel.builder()
                .numeroCartao(mockCartaoEntity.getNumeroCartao())
                .senha(mockCartaoEntity.getSenha())
                .valor(BigDecimal.valueOf(10.50))
                .build();

        when(transacaoRepository.save(any(TransacaoEntity.class))).thenReturn(mapper.map(mockTransacaoModel, TransacaoEntity.class));

        String salvaTransacao = transacaoService.save(mockTransacaoModel);

        Assertions.assertNotNull(salvaTransacao);
        assertEquals("OK", salvaTransacao);
    }

    @Test
    @DisplayName("Localiza todas as Transações com sucesso")
    void testFindAll() {
        SaldoEntity saldo = new SaldoEntity();
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("220220220220220")
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

        List< TransacaoModel > transacoes = transacaoService.findAll();
        List< TransacaoEntity > listaTransacoes = transacoes.stream().map(entity -> mapper.map(entity, TransacaoEntity.class)).collect(Collectors.toList());

        Assertions.assertNotNull(listaTransacoes);
        assertEquals(mockTransacaoEntities, listaTransacoes);
    }

    @Test
    @DisplayName("Localiza todas as Transações inválidas")
    void testFindAllInvalid() {
        SaldoEntity saldo = new SaldoEntity();
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("220220220220220")
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

        when(transacaoRepository.findAll()).thenReturn(new ArrayList<>());

        try {
            List< TransacaoModel > transacoes = transacaoService.findAll();
            List< TransacaoEntity > listaTransacoes = transacoes.stream().map(entity -> mapper.map(entity, TransacaoEntity.class)).collect(Collectors.toList());

            Assertions.assertNotNull(listaTransacoes);
            assertEquals(mockTransacaoEntities, listaTransacoes);
        } catch (Exception e) {
            assertEquals("Nenhuma transação encontrada.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Localiza a Transação por ID com sucesso")
    void testFindTransacaoById() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("220220220220220")
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
        TransacaoEntity transacaoEntity = mapper.map(findTransacao, TransacaoEntity.class);
        transacaoEntity.setCartao(mockCartaoEntity);

        Assertions.assertNotNull(transacaoEntity);
        assertEquals(mockTransacaoEntity, transacaoEntity);
    }

    @Test
    @DisplayName("Localiza o Transação por ID inválido")
    void testFindTransacaoByIdInvalid() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("330330330330330")
                .senha("cccccccccc")
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

        try {
            transacaoService.findById(2L);
        } catch (Exception e) {
            assertEquals("Nenhuma transação encontrada.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Exclui o Transação por ID com sucesso")
    void testDeleteTransacaoById() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("440440440440440")
                .senha("ddddddddddd")
                .saldo(mockSaldoEntity)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findByNumeroCartao("440440440440440")).thenReturn(Optional.of(mockCartaoEntity));

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

    @Test
    @DisplayName("Exclui o Transação por ID inválido")
    void testDeleteTransacaoByIdInvalid() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("550550550550550")
                .senha("eeeeeeeeeeee")
                .saldo(mockSaldoEntity)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findById(1L)).thenReturn(Optional.of(mockCartaoEntity));

        when(transacaoRepository.existsById(1L)).thenReturn(true);

        try {
            transacaoService.deleteById(2L);
        } catch (Exception e) {
            assertEquals("Nenhuma transação encontrada.", e.getMessage());
        }
    }

}
