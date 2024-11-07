package api.repository;

import api.entity.CartaoEntity;
import api.entity.SaldoEntity;
import api.entity.TransacaoEntity;
import api.enums.CartaoStatus;
import api.exception.ModelException;
import api.model.CriaTransacaoModel;
import api.model.TransacaoModel;
import api.model.errors.TransacaoErrors;
import api.service.TransacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransacaoRepositoryTest {

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private CartaoRepository cartaoRepository;

    @Mock
    private SaldoRepository saldoRepository;

    private CriaTransacaoModel criaTransacaoModel;
    private CartaoEntity cartaoEntity;
    private SaldoEntity saldoEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        criaTransacaoModel = new CriaTransacaoModel();
        criaTransacaoModel.setNumeroCartao("1234567890123456");
        criaTransacaoModel.setSenha("1234");
        criaTransacaoModel.setValor(BigDecimal.valueOf(100));

        cartaoEntity = new CartaoEntity();
        cartaoEntity.setNumeroCartao("1234567890123456");
        cartaoEntity.setSenha("1234");
        cartaoEntity.setStatus(CartaoStatus.ATIVO);

        saldoEntity = new SaldoEntity();
        saldoEntity.setValor(BigDecimal.valueOf(500));
        cartaoEntity.setSaldo(saldoEntity);
    }

    @Test
    @DisplayName("Localiza todas as transações")
    void testFindAll() {
        TransacaoEntity transacao1 = new TransacaoEntity();
        transacao1.setId(1L);
        transacao1.setValor(BigDecimal.valueOf(100));
        transacao1.setCartao(cartaoEntity);

        TransacaoEntity transacao2 = new TransacaoEntity();
        transacao2.setId(2L);
        transacao2.setValor(BigDecimal.valueOf(200));
        transacao2.setCartao(cartaoEntity);

        Mockito.when(transacaoRepository.findAll())
                .thenReturn(Arrays.asList(transacao1, transacao2));

        List<TransacaoModel> transacoes = transacaoService.findAll();

        assertNotNull(transacoes);
        assertEquals(2, transacoes.size());
        assertEquals(1L, transacoes.get(0).getId());
        assertEquals(2L, transacoes.get(1).getId());

        Mockito.verify(transacaoRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Cria uma transação com saldo suficiente")
    void testSaveTransacaoComSaldoSuficiente() {
        Mockito.when(cartaoRepository.findByNumeroCartao(criaTransacaoModel.getNumeroCartao()))
                .thenReturn(Optional.of(cartaoEntity));

        Mockito.when(saldoRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(saldoEntity));

        String resultado = transacaoService.save(criaTransacaoModel);

        assertEquals("OK", resultado);

        Mockito.verify(transacaoRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Cria uma transação com saldo insuficiente")
    void testSaveTransacaoComSaldoInsuficiente() {
        criaTransacaoModel.setValor(BigDecimal.valueOf(1000));

        Mockito.when(cartaoRepository.findByNumeroCartao(criaTransacaoModel.getNumeroCartao()))
                .thenReturn(Optional.of(cartaoEntity));

        Mockito.when(saldoRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(saldoEntity));

        ModelException exception = assertThrows(ModelException.class, () -> {
            transacaoService.save(criaTransacaoModel);
        });

        assertTrue(exception.getMessage().contains(TransacaoErrors.INSUFFICIENT_BALANCE.getMessage()));
    }

    @Test
    @DisplayName("Cria uma transação com cartão inativo")
    void testSaveTransacaoComCartaoInativo() {
        cartaoEntity.setStatus(CartaoStatus.INATIVO);

        Mockito.when(cartaoRepository.findByNumeroCartao(criaTransacaoModel.getNumeroCartao()))
                .thenReturn(Optional.of(cartaoEntity));

        ModelException exception = assertThrows(ModelException.class, () -> {
            transacaoService.save(criaTransacaoModel);
        });

        assertTrue(exception.getMessage().contains(TransacaoErrors.INATIVE_CARD.getMessage()));
    }

    @Test
    @DisplayName("Cria uma transação com senha incorreta")
    void testSaveTransacaoComSenhaIncorreta() {
        criaTransacaoModel.setSenha("4321");

        Mockito.when(cartaoRepository.findByNumeroCartao(criaTransacaoModel.getNumeroCartao()))
                .thenReturn(Optional.of(cartaoEntity));

        ModelException exception = assertThrows(ModelException.class, () -> {
            transacaoService.save(criaTransacaoModel);
        });

        assertTrue(exception.getMessage().contains(TransacaoErrors.INVALID_PASSWORD.getMessage()));
    }

    @Test
    @DisplayName("Cria uma transação com número de cartão inválido")
    void testSaveTransacaoComNumeroCartaoInvalido() {
        criaTransacaoModel.setNumeroCartao("0000000000000000");

        Mockito.when(cartaoRepository.findByNumeroCartao(criaTransacaoModel.getNumeroCartao()))
                .thenReturn(Optional.empty());

        ModelException exception = assertThrows(ModelException.class, () -> {
            transacaoService.save(criaTransacaoModel);
        });

        assertTrue(exception.getMessage().contains(TransacaoErrors.INVALID_NUMBER_CARD.getMessage()));
    }

    @Test
    @DisplayName("Exclui uma transação")
    void testDeleteTransacao() {
        TransacaoEntity transacaoEntity = new TransacaoEntity();
        transacaoEntity.setId(1L);
        transacaoEntity.setValor(BigDecimal.valueOf(100));
        transacaoEntity.setCartao(cartaoEntity);

        Mockito.when(transacaoRepository.findById(1L))
                .thenReturn(Optional.of(transacaoEntity));

        Mockito.when(cartaoRepository.findByNumeroCartao(cartaoEntity.getNumeroCartao()))
                .thenReturn(Optional.of(cartaoEntity));

        Mockito.when(saldoRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(saldoEntity));

        String resultado = transacaoService.deleteById(1L);

        assertEquals("Transação excluída com sucesso.", resultado);

        Mockito.verify(transacaoRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Exclui uma transação não existente")
    void testDeleteTransacaoNaoExistente() {
        Mockito.when(transacaoRepository.findById(1L))
                .thenReturn(Optional.empty());

        ModelException exception = assertThrows(ModelException.class, () -> {
            transacaoService.deleteById(1L);
        });

        assertTrue(exception.getMessage().contains(TransacaoErrors.NOT_FOUND.getMessage()));
    }

}
