package api.service;

import api.entity.CartaoEntity;
import api.entity.SaldoEntity;
import api.enums.CartaoStatus;
import api.exception.CartaoJaExisteException;
import api.exception.ModelException;
import api.model.CartaoModel;
import api.model.CartaoRespostaModel;
import api.model.CriaCartaoModel;
import api.model.errors.CartaoErrors;
import api.repository.CartaoRepository;
import api.repository.SaldoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    public Optional<BigDecimal> findCartaoByNumeroCartao(String numeroCartao) {
        return cartaoRepository.findByNumeroCartao(numeroCartao)
                .map(CartaoEntity::getSaldo)
                .filter(Objects::nonNull)
                .map(SaldoEntity::getValor);
    }

    public CartaoModel findCartaoById(Long id) {
        CartaoEntity cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ModelException(CartaoErrors.NOT_FOUND));
        return toModel(cartao);
    }

    public List<CartaoModel> findAllByOrderByNumeroCartaoAsc() {
        List<CartaoEntity> cartaoEntities = cartaoRepository.findAllByOrderByNumeroCartaoAsc();
        return cartaoEntities.stream().map(this::toModel).collect(Collectors.toList());
    }

    public List<CartaoModel> findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus status) {
        List<CartaoEntity> cartaoEntities = cartaoRepository.findAllByStatusOrderByNumeroCartaoAsc(status);
        return cartaoEntities.stream().map(this::toModel).collect(Collectors.toList());
    }

    public CartaoRespostaModel save(CriaCartaoModel criaCartaoModel) {
        Optional.of(criaCartaoModel.getNumeroCartao())
                .filter(n -> cartaoRepository.findByNumeroCartao(n).isPresent())
                .ifPresent(n -> {
                    throw new CartaoJaExisteException(criaCartaoModel.getNumeroCartao(), criaCartaoModel.getSenha());
                });

        try {
            SaldoEntity saldoEntity = new SaldoEntity();
            saldoRepository.save(saldoEntity);

            CartaoEntity cartaoEntity = CartaoEntity.builder()
                    .numeroCartao(criaCartaoModel.getNumeroCartao())
                    .senha(criaCartaoModel.getSenha())
                    .saldo(saldoEntity)
                    .status(CartaoStatus.ATIVO)
                    .build();

            cartaoRepository.save(cartaoEntity);

            return CartaoRespostaModel.builder()
                    .numeroCartao(cartaoEntity.getNumeroCartao())
                    .senha(cartaoEntity.getSenha())
                    .build();
        } catch (RuntimeException e) {
            throw new ModelException(CartaoErrors.ERROR_CREATING, e);
        }
    }

    public CartaoModel update(Long id, CartaoEntity cartaoEntity) {
        CartaoEntity cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ModelException(CartaoErrors.NOT_FOUND));

        try {
            SaldoEntity saldoEntity = Optional.ofNullable(cartao.getSaldo())
                    .map(saldo -> saldoRepository.findById(saldo.getId()).orElse(new SaldoEntity()))
                    .orElse(new SaldoEntity());

            cartaoEntity.setId(id);
            cartaoEntity.setSaldo(saldoEntity);
            cartaoEntity.setStatus(Optional.ofNullable(cartaoEntity.getStatus()).orElse(CartaoStatus.ATIVO));
            cartaoEntity.setCreatedAt(cartao.getCreatedAt());
            cartaoEntity = cartaoRepository.save(cartaoEntity);
            return toModel(cartaoEntity);
        } catch (RuntimeException e) {
            throw new ModelException(CartaoErrors.ERROR_CREATING, e);
        }
    }

    public String deleteCartaoById(Long id) {
        Optional.of(id).filter(cartaoRepository::existsById)
                .orElseThrow(() -> new ModelException(CartaoErrors.NOT_FOUND));
        cartaoRepository.deleteById(id);
        return "Cartão excluído com sucesso.";
    }

    public boolean isCardExist(CartaoEntity cartaoEntity) {
        return cartaoRepository.findByNumeroCartao(cartaoEntity.getNumeroCartao()).isPresent();
    }

    private CartaoModel toModel(CartaoEntity entity) {
        BigDecimal saldo = Optional.ofNullable(entity.getSaldo())
                .map(SaldoEntity::getValor)
                .orElse(BigDecimal.ZERO);
        return CartaoModel.builder()
                .id(entity.getId())
                .numeroCartao(entity.getNumeroCartao())
                .saldo(saldo)
                .status(entity.getStatus())
                .build();
    }
}
