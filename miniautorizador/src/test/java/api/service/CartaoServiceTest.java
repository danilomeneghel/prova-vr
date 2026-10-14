package api.service;

import api.ApplicationTests;
import api.entity.CartaoEntity;
import api.entity.SaldoEntity;
import api.enums.CartaoStatus;
import api.exception.CartaoJaExisteException;
import api.model.errors.CartaoErrors;
import api.exception.ModelException;
import api.model.CartaoModel;
import api.model.CartaoRespostaModel;
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
    @DisplayName("Cria o cartão com sucesso - retorna CartaoRespostaModel com { senha, numeroCartao }")
    void testSaveCartao() {
        CriaCartaoModel mockCartaoModel = CriaCartaoModel.builder()
                .numeroCartao("1111111111111111")
                .senha("1234")
                .build();

        CartaoEntity cartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("1111111111111111")
                .senha("1234")
                .saldo(new SaldoEntity())
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findByNumeroCartao("1111111111111111")).thenReturn(Optional.empty());
        when(cartaoRepository.save(any(CartaoEntity.class))).thenReturn(cartaoEntity);
        when(saldoRepository.save(any(SaldoEntity.class))).thenReturn(new SaldoEntity());

        CartaoRespostaModel savedCartaoModel = cartaoService.save(mockCartaoModel);

        assertNotNull(savedCartaoModel);
        assertEquals(mockCartaoModel.getNumeroCartao(), savedCartaoModel.getNumeroCartao());
        assertEquals(mockCartaoModel.getSenha(), savedCartaoModel.getSenha());
    }

    @Test
    @DisplayName("Falha ao criar o cartão por erro ao salvar saldo")
    void testSaveCartaoError() {
        CriaCartaoModel mockCartaoModel = CriaCartaoModel.builder()
                .numeroCartao("1111111111111111")
                .senha("1234")
                .build();

        when(cartaoRepository.findByNumeroCartao("1111111111111111")).thenReturn(Optional.empty());
        when(saldoRepository.save(any(SaldoEntity.class))).thenThrow(new RuntimeException("Erro ao salvar saldo"));

        try {
            cartaoService.save(mockCartaoModel);
            fail("Esperado ModelException, mas não ocorreu");
        } catch (ModelException exception) {
            assertEquals(CartaoErrors.ERROR_CREATING.getMessage(), exception.getMessage());
        }
    }

    @Test
    @DisplayName("Lança CartaoJaExisteException quando o cartão já existir")
    void testSaveCartaoCardExists() {
        CartaoEntity cartaoEntity = CartaoEntity.builder()
                .numeroCartao("3333333333333333")
                .senha("1234")
                .status(CartaoStatus.ATIVO)
                .build();

        when(cartaoRepository.findByNumeroCartao("3333333333333333")).thenReturn(Optional.of(cartaoEntity));

        CriaCartaoModel criaCartaoModel = new CriaCartaoModel();
        criaCartaoModel.setNumeroCartao("3333333333333333");
        criaCartaoModel.setSenha("1234");

        CartaoJaExisteException exception = assertThrows(CartaoJaExisteException.class,
                () -> cartaoService.save(criaCartaoModel));

        assertEquals("3333333333333333", exception.getCartaoRespostaModel().getNumeroCartao());
        assertEquals("1234", exception.getCartaoRespostaModel().getSenha());
    }

    @Test
    @DisplayName("Atualiza o cartão com sucesso")
    void testUpdateCartao() {
        SaldoEntity saldo = new SaldoEntity();
        saldo.setId(1L);

        CartaoEntity existingCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("1111111111111111")
                .senha("senha123")
                .saldo(saldo)
                .status(CartaoStatus.ATIVO)
                .build();

        CartaoEntity updatedCartaoEntity = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("3333333333333333")
                .senha("senha456")
                .saldo(saldo)
                .status(CartaoStatus.INATIVO)
                .build();

        when(cartaoRepository.existsById(1L)).thenReturn(true);
        when(cartaoRepository.findById(1L)).thenReturn(Optional.of(existingCartaoEntity));
        when(saldoRepository.findById(1L)).thenReturn(Optional.of(saldo));
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
        updatedCartaoEntity.setNumeroCartao("1234567890123456");
        updatedCartaoEntity.setSenha("4321");

        ModelException exception = assertThrows(ModelException.class,
                () -> cartaoService.update(999L, updatedCartaoEntity));
        assertEquals(CartaoErrors.NOT_FOUND.getMessage(), exception.getMessage());
    }

}
