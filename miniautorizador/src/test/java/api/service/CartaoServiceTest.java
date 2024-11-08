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
    @DisplayName("Falha ao criar o cartão por erro ao salvar saldo")
    void testSaveCartaoError() {
        CriaCartaoModel mockCartaoModel = CriaCartaoModel.builder()
                .numeroCartao(Long.valueOf("1111111111111111"))
                .senha("senha123")
                .build();

        CartaoEntity cartaoEntity = mapper.map(mockCartaoModel, CartaoEntity.class);

        when(cartaoRepository.save(any(CartaoEntity.class))).thenReturn(cartaoEntity);
        when(saldoRepository.save(any(SaldoEntity.class))).thenThrow(new RuntimeException("Erro ao salvar saldo"));

        try {
            cartaoService.save(mockCartaoModel);
            fail("Esperado ModelException, mas não ocorreu");
        } catch (ModelException exception) {
            assertEquals(CartaoErrors.ERROR_CREATING.getMessage(), exception.getMessage());
        }
    }

    @Test
    @DisplayName("Lança exceção quando o cartão já existir")
    void testSaveCartaoCardExists() {
        CartaoEntity cartaoEntity = CartaoEntity.builder()
                .numeroCartao(Long.valueOf("3333333333333333"))
                .senha("senha123")
                .status(CartaoStatus.INATIVO)
                .build();

        when(cartaoRepository.findByNumeroCartao(anyLong())).thenReturn(Optional.of(cartaoEntity));

        CriaCartaoModel criaCartaoModel = new CriaCartaoModel();
        criaCartaoModel.setNumeroCartao(123456789012L);
        criaCartaoModel.setSenha("senha123");

        ModelException exception = assertThrows(ModelException.class, () -> cartaoService.save(criaCartaoModel));
        assert exception.getMessage().equals(CartaoErrors.CARD_EXISTS.getMessage());
    }

    @Test
    @DisplayName("Lança exceção quando o número do cartão for inválido (menor que 13 dígitos)")
    void testSaveCartaoInvalidNumber() {
        CriaCartaoModel criaCartaoModel = new CriaCartaoModel();
        criaCartaoModel.setNumeroCartao(1234567890L);
        criaCartaoModel.setSenha("1234");

        ModelException exception = assertThrows(ModelException.class, () -> cartaoService.save(criaCartaoModel));
        assert exception.getMessage().equals(CartaoErrors.INVALID_NUMBER_CARD.getMessage());
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
    @DisplayName("Lança exceção quando tentar atualizar um cartão inexistente")
    void testUpdateCartaoNotFound() {
        when(cartaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        CartaoEntity updatedCartaoEntity = new CartaoEntity();
        updatedCartaoEntity.setId(999L);
        updatedCartaoEntity.setNumeroCartao(123456789012L);
        updatedCartaoEntity.setSenha("4321");

        ModelException exception = assertThrows(ModelException.class, () -> cartaoService.update(999L, updatedCartaoEntity));
        assert exception.getMessage().equals(CartaoErrors.NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("Lança exceção quando o número do cartão for inválido durante a atualização")
    void testUpdateCartaoInvalidNumber() {
        CartaoEntity cartaoEntity = CartaoEntity.builder()
                .numeroCartao(Long.valueOf("3333333333333333"))
                .senha("senha456")
                .status(CartaoStatus.INATIVO)
                .build();

        CartaoEntity updatedCartaoEntity = new CartaoEntity();
        updatedCartaoEntity.setId(cartaoEntity.getId());
        updatedCartaoEntity.setNumeroCartao(123L);
        updatedCartaoEntity.setSenha("senha456");

        when(cartaoRepository.findById(cartaoEntity.getId())).thenReturn(Optional.of(cartaoEntity));

        ModelException exception = assertThrows(ModelException.class, () -> cartaoService.update(cartaoEntity.getId(), updatedCartaoEntity));

        assertEquals(CartaoErrors.INVALID_NUMBER_CARD.getMessage(), exception.getMessage());
        verify(cartaoRepository, times(0)).save(any(CartaoEntity.class));
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

}
