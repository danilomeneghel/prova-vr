package api.service;

import api.ApplicationTests;
import api.entity.CartaoEntity;
import api.entity.SaldoEntity;
import api.enums.CartaoStatus;
import api.model.errors.CartaoErrors;
import api.exception.ModelException;
import api.model.CartaoModel;
import api.model.CriaCartaoModel;
import api.repository.CartaoRepository;
import api.repository.SaldoRepository;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartaoServiceTest extends ApplicationTests {

    @Autowired
    private CartaoService cartaoService;

    @MockBean
    private CartaoRepository cartaoRepository;

    @MockBean
    private SaldoRepository saldoRepository;

    private ModelMapper mapper = new ModelMapper();

    @Test
    @DisplayName("Cria o cartão com sucesso")
    void testSaveCartao() {
        CriaCartaoModel mockCartaoModel = CriaCartaoModel.builder()
                .numeroCartao(Long.valueOf("1111111111111111"))
                .senha("senha123")
                .build();

        CartaoEntity cartaoEntity = mapper.map(mockCartaoModel, CartaoEntity.class);

        when(cartaoRepository.save(any(CartaoEntity.class))).thenReturn(cartaoEntity);
        when(saldoRepository.save(any(SaldoEntity.class))).thenReturn(new SaldoEntity());

        CartaoModel savedCartaoModel = cartaoService.save(mockCartaoModel);

        assertNotNull(savedCartaoModel);
        assertEquals(mockCartaoModel.getNumeroCartao(), savedCartaoModel.getNumeroCartao());
    }

    @Test
    @DisplayName("Lança erro ao tentar criar um cartão com número de cartão já existente")
    void testSaveCartaoCardExists() {
        CriaCartaoModel mockCartaoModel = CriaCartaoModel.builder()
                .numeroCartao(Long.valueOf("1111111111111111"))
                .senha("senha123")
                .build();

        CartaoEntity cartaoEntity = mapper.map(mockCartaoModel, CartaoEntity.class);

        when(cartaoRepository.findByNumeroCartao(mockCartaoModel.getNumeroCartao())).thenReturn(Optional.of(cartaoEntity));

        ModelException exception = assertThrows(ModelException.class, () -> {
            cartaoService.save(mockCartaoModel);
        });

        assertEquals(CartaoErrors.CARD_EXISTS.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Lança erro ao criar cartão com número inválido")
    void testSaveCartaoInvalidNumber() {
        CriaCartaoModel mockCartaoModel = CriaCartaoModel.builder()
                .numeroCartao(Long.valueOf("1234"))
                .senha("senha123")
                .build();

        ModelException exception = assertThrows(ModelException.class, () -> {
            cartaoService.save(mockCartaoModel);
        });

        assertEquals(CartaoErrors.INVALID_NUMBER_CARD.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Falha ao criar o cartão por erro ao salvar saldo")
    void testSaveCartaoErrorCreating() {
        CriaCartaoModel mockCartaoModel = CriaCartaoModel.builder()
                .numeroCartao(Long.valueOf("1111111111111111"))
                .senha("senha123")
                .build();

        CartaoEntity cartaoEntity = mapper.map(mockCartaoModel, CartaoEntity.class);

        when(cartaoRepository.save(any(CartaoEntity.class))).thenReturn(cartaoEntity);
        when(saldoRepository.save(any(SaldoEntity.class))).thenThrow(new RuntimeException("Erro ao salvar saldo"));

        ModelException exception = assertThrows(ModelException.class, () -> {
            cartaoService.save(mockCartaoModel);
        });

        assertEquals(CartaoErrors.ERROR_CREATING.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Atualiza o cartão com sucesso")
    void testUpdateCartao() {
        CartaoEntity existingCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao(Long.valueOf("1111111111111111"))
                .senha("senha123")
                .saldo(new SaldoEntity())
                .status(CartaoStatus.ATIVO)
                .build();

        CartaoEntity updatedCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao(Long.valueOf("3333333333333333"))
                .senha("senha456")
                .saldo(new SaldoEntity())
                .status(CartaoStatus.INATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);
        when(cartaoRepository.findById(1L)).thenReturn(Optional.of(existingCartaoEntity));
        when(cartaoRepository.save(any(CartaoEntity.class))).thenReturn(updatedCartaoEntity);

        CartaoModel updatedCartaoModel = cartaoService.update(1L, updatedCartaoEntity);

        assertNotNull(updatedCartaoModel);
        assertEquals(updatedCartaoEntity.getNumeroCartao(), updatedCartaoModel.getNumeroCartao());
        assertEquals(updatedCartaoEntity.getStatus(), updatedCartaoModel.getStatus());
    }

    @Test
    @DisplayName("Lança erro ao tentar atualizar cartão com ID inválido")
    void testUpdateCartaoIdInvalid() {
        CartaoEntity cartaoEntity = CartaoEntity.builder()
                .numeroCartao(Long.valueOf("3333333333333333"))
                .senha("senha456")
                .status(CartaoStatus.INATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);
        when(cartaoRepository.findById(2L)).thenReturn(Optional.empty());

        ModelException exception = assertThrows(ModelException.class, () -> {
            cartaoService.update(2L, cartaoEntity);
        });

        assertEquals(CartaoErrors.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Lança erro ao tentar atualizar um cartão inexistente")
    void testUpdateCartaoNotFound() {
        CartaoEntity cartaoEntity = CartaoEntity.builder()
                .numeroCartao(Long.valueOf("3333333333333333"))
                .senha("senha456")
                .status(CartaoStatus.INATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(false);

        ModelException exception = assertThrows(ModelException.class, () -> {
            cartaoService.update(1L, cartaoEntity);
        });

        assertEquals(CartaoErrors.NOT_FOUND.getMessage(), exception.getMessage());
    }
}
