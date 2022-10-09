package api.service;

import api.ApplicationTests;
import api.entity.CartaoEntity;
import api.entity.SaldoEntity;
import api.enums.CartaoStatus;
import api.model.CartaoModel;
import api.model.CriaCartaoModel;
import api.repository.CartaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CartaoServiceTest extends ApplicationTests {

    @Autowired
    private CartaoService cartaoService;

    @MockBean
    private CartaoRepository cartaoRepository;

    private SaldoEntity saldo = new SaldoEntity();

    private ModelMapper mapper = new ModelMapper();

    @Test
    @DisplayName("Cria o Cartão com sucesso")
    void testSaveCartao() {
        CriaCartaoModel mockCartaoModel = CriaCartaoModel.builder()
                .numeroCartao("1111111111")
                .senha(null)
                .build();

        CartaoEntity cartaoEntity = mapper.map(mockCartaoModel, CartaoEntity.class);

        when(cartaoRepository.save(any(CartaoEntity.class))).thenReturn(cartaoEntity);

        CartaoModel saveCartaoModel = cartaoService.save(mockCartaoModel);
        CriaCartaoModel criaCartaoModel = mapper.map(saveCartaoModel, CriaCartaoModel.class);

        Assertions.assertNotNull(cartaoEntity);
        assertEquals(mockCartaoModel, criaCartaoModel);
    }

    @Test
    @DisplayName("Localiza todos os Cartões por Status com sucesso")
    void testFindAllByStatus() {
        List<CartaoEntity> mockListCartoesEntities = Stream.of(
                        CartaoEntity.builder()
                                .id(1L)
                                .numeroCartao("1111111111")
                                .saldo(saldo)
                                .status(CartaoStatus.ATIVO)
                                .build(),
                        CartaoEntity.builder()
                                .id(2L)
                                .numeroCartao("2222222222")
                                .saldo(saldo)
                                .status(CartaoStatus.ATIVO)
                                .build())
                .collect(Collectors.toList());

        when(cartaoRepository.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus.ATIVO))
                .thenReturn(mockListCartoesEntities);

        List<CartaoModel> cartoes = cartaoService.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus.ATIVO);
        List< CartaoEntity > listaCartoes = cartoes.stream().map(entity -> mapper.map(entity, CartaoEntity.class)).collect(Collectors.toList());

        Assertions.assertNotNull(listaCartoes);
        assertEquals(mockListCartoesEntities, listaCartoes);
    }

    @Test
    @DisplayName("Localiza todos os Cartões por Status inválido")
    void testFindAllByStatusInvalid() {
        List<CartaoEntity> mockListCartoesEntities = Stream.of(
                        CartaoEntity.builder()
                                .id(1L)
                                .numeroCartao("1111111111")
                                .saldo(saldo)
                                .status(CartaoStatus.ATIVO)
                                .build(),
                        CartaoEntity.builder()
                                .id(2L)
                                .numeroCartao("2222222222")
                                .saldo(saldo)
                                .status(CartaoStatus.ATIVO)
                                .build())
                .collect(Collectors.toList());

        when(cartaoRepository.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus.ATIVO))
                .thenReturn(mockListCartoesEntities);

        try {
            List<CartaoModel> cartoes = cartaoService.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus.INATIVO);
            List< CartaoEntity > listaCartoes = cartoes.stream().map(entity -> mapper.map(entity, CartaoEntity.class)).collect(Collectors.toList());
            assertNotEquals(mockListCartoesEntities, listaCartoes);
        } catch (Exception e) {
            assertEquals("Nenhum cartão encontrado.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Localiza o Cartão por ID com sucesso")
    void testFindCartaoById() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("1111111111")
                .saldo(saldo)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findById(1L)).thenReturn(Optional.of(mockCartaoEntity));

        CartaoModel findCartao = cartaoService.findCartaoById(1L);
        CartaoEntity cartaoEntity = mapper.map(findCartao, CartaoEntity.class);

        Assertions.assertNotNull(cartaoEntity);
        assertEquals(mockCartaoEntity, cartaoEntity);
    }

    @Test
    @DisplayName("Localiza o Cartão por ID inválido")
    void testFindCartaoByIdInvalid() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("1111111111")
                .saldo(saldo)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findById(1L)).thenReturn(Optional.of(mockCartaoEntity));

        try {
            cartaoService.findCartaoById(2L);
        } catch (Exception e) {
            assertEquals("Nenhum cartão encontrado.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Localiza o Cartão por Número do Cartão com sucesso")
    void testFindCartaoByNumber() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .numeroCartao("1111111111")
                .saldo(saldo)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findByNumeroCartao("1111111111")).thenReturn(Optional.of(mockCartaoEntity));

        BigDecimal findCartao = cartaoService.findCartaoByNumeroCartao("1111111111");

        Assertions.assertNotNull(findCartao);
        assertEquals(mockCartaoEntity.getSaldo().getValor(), findCartao);
    }

    @Test
    @DisplayName("Localiza o Cartão por Número do Cartão inválido")
    void testFindCartaoByNumberInvalid() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .numeroCartao("1111111111")
                .saldo(saldo)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findByNumeroCartao("1111111111")).thenReturn(Optional.of(mockCartaoEntity));

        try {
            cartaoService.findCartaoByNumeroCartao("555555555");
        } catch (Exception e) {
            assertEquals("", e.getMessage());
        }
    }

    @Test
    @DisplayName("Altera o Cartão por ID com sucesso")
    void testUpdateCartao() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .numeroCartao("1111111111")
                .senha("xxxxxxxx")
                .saldo(saldo)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);

        mockCartaoEntity.setNumeroCartao("3333333333");
        when(cartaoRepository.save(any(CartaoEntity.class))).thenReturn(mockCartaoEntity);

        CartaoModel updateCartaoModel = cartaoService.update(1L, mockCartaoEntity);
        CartaoEntity cartaoEntity = mapper.map(updateCartaoModel, CartaoEntity.class);
        cartaoEntity.setSaldo(mockCartaoEntity.getSaldo());
        cartaoEntity.setSenha(mockCartaoEntity.getSenha());

        Assertions.assertNotNull(cartaoEntity);
        assertEquals(mockCartaoEntity, cartaoEntity);
    }

    @Test
    @DisplayName("Altera o Cartão por ID inválido")
    void testUpdateCartaoIdInvalid() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .numeroCartao("1111111111")
                .senha("2222222222")
                .saldo(saldo)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);

        mockCartaoEntity.setNumeroCartao("44444444444");
        when(cartaoRepository.save(any(CartaoEntity.class))).thenReturn(mockCartaoEntity);

        try {
            cartaoService.update(2L, mockCartaoEntity);
        } catch (Exception e) {
            assertEquals("Nenhum cartão encontrado.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Exclui o Cartão por ID com sucesso")
    void testDeleteCartaoById() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .numeroCartao("1111111111")
                .senha("2222222222")
                .saldo(saldo)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);

        String deleteCartao = cartaoService.deleteCartaoById(1L);

        Assertions.assertNotNull(deleteCartao);
        assertEquals("Cartão excluído com sucesso.", deleteCartao);
    }

    @Test
    @DisplayName("Exclui o Cartão por ID inválido")
    void testDeleteCartaoByIdInvalid() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .numeroCartao("1111111111")
                .senha("2222222222")
                .saldo(saldo)
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);

        try {
            cartaoService.deleteCartaoById(2L);
        } catch (Exception e) {
            assertEquals("Nenhum cartão encontrado.", e.getMessage());
        }
    }

}
