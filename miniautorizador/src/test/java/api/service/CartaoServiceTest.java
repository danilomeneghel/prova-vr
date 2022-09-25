package api.service;

import api.ApplicationTests;
import api.entity.CartaoEntity;
import api.enums.CartaoStatus;
import api.exception.RecordNotFoundException;
import api.model.CartaoModel;
import api.repository.CartaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    @Test
    @DisplayName("Cria o Cartão com sucesso")
    void testSaveCartao() throws RecordNotFoundException {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .numeroCartao("1111111111")
                .senha("2222222222")
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.save(any(CartaoEntity.class))).thenReturn(mockCartaoEntity);

        CartaoModel saveCartaoEntity = cartaoService.save(mockCartaoEntity);

        Assertions.assertNotNull(saveCartaoEntity);
        assertEquals(mockCartaoEntity, saveCartaoEntity);
    }

    @Test
    @DisplayName("Localiza todos os Cartões por Status com sucesso")
    void testFindAllByStatus() {
        List<CartaoEntity> mockListCartaoEntities = Stream.of(
                        CartaoEntity.builder()
                                .numeroCartao("1111111111")
                                .senha("2222222222")
                                .status(CartaoStatus.ATIVO)
                                .build(),
                        CartaoEntity.builder()
                                .numeroCartao("1111111111")
                                .senha("2222222222")
                                .status(CartaoStatus.ATIVO)
                                .build())
                .collect(Collectors.toList());

        when(cartaoRepository.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus.ATIVO))
                .thenReturn(mockListCartaoEntities);

        List<CartaoModel> listCartaos = cartaoService.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus.ATIVO);

        Assertions.assertNotNull(listCartaos);
        assertEquals(mockListCartaoEntities, listCartaos);
    }

    @Test
    @DisplayName("Localiza todos os Cartões por Status inválido")
    void testFindAllByStatusInvalid() {
        List<CartaoEntity> mockListCartaoEntities = Stream.of(
                        CartaoEntity.builder()
                                .numeroCartao("1111111111")
                                .senha("2222222222")
                                .status(CartaoStatus.ATIVO)
                                .build(),
                        CartaoEntity.builder()
                                .numeroCartao("1111111111")
                                .senha("2222222222")
                                .status(CartaoStatus.ATIVO)
                                .build())
                .collect(Collectors.toList());

        when(cartaoRepository.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus.ATIVO))
                .thenReturn(mockListCartaoEntities);

        List<CartaoModel> listCartaos = cartaoService.findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus.INATIVO);

        assertNotEquals(mockListCartaoEntities, listCartaos);
    }

    @Test
    @DisplayName("Localiza o Cartão por ID com sucesso")
    void testFindCartaoById() throws RecordNotFoundException {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .numeroCartao("1111111111")
                .senha("2222222222")
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findById(1L)).thenReturn(Optional.of(mockCartaoEntity));

        CartaoModel findCartao = cartaoService.findCartaoById(1L);

        Assertions.assertNotNull(findCartao);
        assertEquals(mockCartaoEntity, findCartao);
    }

    @Test
    @DisplayName("Localiza o Cartão por ID inválido")
    void testFindCartaoByIdInvalid() throws RecordNotFoundException {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .numeroCartao("1111111111")
                .senha("2222222222")
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findById(1L)).thenReturn(Optional.of(mockCartaoEntity));

        try {
            cartaoService.findCartaoById(2L);
        } catch (Exception e) {
            assertEquals("ID não encontrado.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Altera o Cartão por ID com sucesso")
    void testUpdateCartao() throws RecordNotFoundException {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .numeroCartao("1111111111")
                .senha("2222222222")
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);

        mockCartaoEntity.setNumeroCartao("3333333333");
        when(cartaoRepository.save(any(CartaoEntity.class))).thenReturn(mockCartaoEntity);

        CartaoModel updateCartaoEntity = cartaoService.update(1L, mockCartaoEntity);

        Assertions.assertNotNull(updateCartaoEntity);
        assertEquals(mockCartaoEntity, updateCartaoEntity);
    }

    @Test
    @DisplayName("Altera o Cartão por ID inválido")
    void testUpdateCartaoIdInvalid() {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .numeroCartao("1111111111")
                .senha("2222222222")
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);

        mockCartaoEntity.setNumeroCartao("44444444444");
        when(cartaoRepository.save(any(CartaoEntity.class))).thenReturn(mockCartaoEntity);

        try {
            cartaoService.update(2L, mockCartaoEntity);
        } catch (Exception e) {
            assertEquals("ID não encontrado.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Exclui o Cartão por ID com sucesso")
    void testDeleteCartaoById() throws RecordNotFoundException {
        CartaoEntity mockCartaoEntity = CartaoEntity.builder()
                .numeroCartao("1111111111")
                .senha("2222222222")
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
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);

        try {
            cartaoService.deleteCartaoById(2L);
        } catch (Exception e) {
            assertEquals("ID não encontrado.", e.getMessage());
        }
    }

}
