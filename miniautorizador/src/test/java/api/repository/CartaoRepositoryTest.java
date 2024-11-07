package api.repository;

import api.entity.CartaoEntity;
import api.entity.SaldoEntity;
import api.enums.CartaoStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CartaoRepositoryTest {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    private SaldoEntity saldoEntity;
    private CartaoEntity cartaoEntity;

    @BeforeEach
    void setUp() {
        saldoEntity = new SaldoEntity();
        saldoEntity = saldoRepository.save(saldoEntity);

        cartaoEntity = new CartaoEntity();
        cartaoEntity.setNumeroCartao("1234567890123456");
        cartaoEntity.setSaldo(saldoEntity);
        cartaoEntity.setStatus(CartaoStatus.ATIVO);
    }

    @Test
    @DisplayName("Salva e localiza pelo número do cartão")
    void testSaveAndFindByNumeroCartao() {
        CartaoEntity savedCartao = cartaoRepository.save(cartaoEntity);

        Optional<CartaoEntity> retrievedCartao = cartaoRepository.findByNumeroCartao(savedCartao.getNumeroCartao());

        assertTrue(retrievedCartao.isPresent());
        assertEquals(savedCartao.getNumeroCartao(), retrievedCartao.get().getNumeroCartao());
        assertEquals(savedCartao.getSaldo().getValor(), retrievedCartao.get().getSaldo().getValor());
        assertEquals(savedCartao.getStatus(), retrievedCartao.get().getStatus());
    }

    @Test
    @DisplayName("Salva cartão com saldo nulo, o que deve gerar uma exceção")
    void testSaveCartaoWithNullSaldo() {
        CartaoEntity cartaoComSaldoNulo = new CartaoEntity();
        cartaoComSaldoNulo.setNumeroCartao("9876543210987654");
        cartaoComSaldoNulo.setSaldo(null);
        cartaoComSaldoNulo.setStatus(CartaoStatus.ATIVO);

        assertThrows(DataIntegrityViolationException.class, () -> {
            cartaoRepository.save(cartaoComSaldoNulo);
        });
    }

    @Test
    @DisplayName("Localiza todos os cartões ordenados pelo número do cartão em ordem crescente")
    void testFindAllByOrderByNumeroCartaoAsc() {
        cartaoRepository.save(cartaoEntity);

        Iterable<CartaoEntity> cartoes = cartaoRepository.findAllByOrderByNumeroCartaoAsc();

        assertNotNull(cartoes);
        assertTrue(cartoes.iterator().hasNext());
    }

    @Test
    @DisplayName("Localiza os cartões ativos ordenados pelo número do cartão em ordem crescente")
    void testFindAllByStatusOrderByNumeroCartaoAsc() {
        cartaoRepository.save(cartaoEntity);

        Iterable<CartaoEntity> cartoesAtivos = cartaoRepository.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus.ATIVO);

        assertNotNull(cartoesAtivos);
        assertTrue(cartoesAtivos.iterator().hasNext());
    }

    @Test
    @DisplayName("Salva e localiza um cartão com status INATIVO")
    void testCartaoStatusInativo() {
        CartaoEntity cartaoInativo = new CartaoEntity();
        cartaoInativo.setNumeroCartao("1111222233334444");
        cartaoInativo.setSaldo(saldoEntity);
        cartaoInativo.setStatus(CartaoStatus.INATIVO);

        cartaoRepository.save(cartaoInativo);

        Iterable<CartaoEntity> cartoesInativos = cartaoRepository.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus.INATIVO);

        assertNotNull(cartoesInativos);
        assertTrue(cartoesInativos.iterator().hasNext());
    }
}
