package api.service;

import api.entity.CartaoEntity;
import api.entity.SaldoEntity;
import api.enums.CartaoStatus;
import api.exception.ModelException;
import api.model.CartaoModel;
import api.model.CriaCartaoModel;
import api.model.errors.CartaoErrors;
import api.repository.CartaoRepository;
import api.repository.SaldoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    private ModelMapper mapper = new ModelMapper();

    public BigDecimal findCartaoByNumeroCartao(Long numeroCartao) {
        CartaoEntity cartao = cartaoRepository.findByNumeroCartao(numeroCartao).orElseThrow(() -> new ModelException(CartaoErrors.INVALID_NUMBER_CARD));
        return cartao.getSaldo().getValor();
    }

    public CartaoModel findCartaoById(Long id) {
        CartaoEntity cartao = cartaoRepository.findById(id).orElseThrow(() -> new ModelException(CartaoErrors.NOT_FOUND));
        return mapper.map(cartao, CartaoModel.class);
    }

    public List<CartaoModel> findAllByOrderByNumeroCartaoAsc() {
        List<CartaoEntity> cartaoEntities = cartaoRepository.findAllByOrderByNumeroCartaoAsc();
        return cartaoEntities.stream().map(entity -> mapper.map(entity, CartaoModel.class)).collect(Collectors.toList());
    }

    public List<CartaoModel> findAllByStatusOrderByNumeroCartaoAsc(CartaoStatus status) {
        List<CartaoEntity> cartaoEntities = cartaoRepository.findAllByStatusOrderByNumeroCartaoAsc(status);
        return cartaoEntities.stream().map(entity -> mapper.map(entity, CartaoModel.class)).collect(Collectors.toList());
    }

    public CartaoModel save(CriaCartaoModel criaCartaoModel) {
        CartaoEntity cartaoEntity = mapper.map(criaCartaoModel, CartaoEntity.class);
        Optional.of(cartaoEntity)
                .filter(this::isCardExist)
                .ifPresent(c -> {
                    throw new ModelException(CartaoErrors.CARD_EXISTS);
                });
        Optional.of(cartaoEntity.getNumeroCartao())
                .filter(numero -> numero.toString().length() >= 13 && numero.toString().length() <= 19)
                .orElseThrow(() -> new ModelException(CartaoErrors.INVALID_NUMBER_CARD));
        try {
            SaldoEntity saldoEntity = new SaldoEntity();
            saldoRepository.save(saldoEntity);
            cartaoEntity.setSaldo(saldoEntity);
            cartaoEntity.setStatus(CartaoStatus.ATIVO);
            cartaoEntity = cartaoRepository.save(cartaoEntity);
            return mapper.map(cartaoEntity, CartaoModel.class);
        } catch (RuntimeException e) {
            throw new ModelException(CartaoErrors.ERROR_CREATING, e);
        }
    }

    public CartaoModel update(Long id, CartaoEntity cartaoEntity) {
        CartaoEntity cartao = cartaoRepository.findById(id).orElseThrow(() -> new ModelException(CartaoErrors.NOT_FOUND));
        Optional.of(cartaoEntity.getNumeroCartao())
                .filter(numero -> numero.toString().length() >= 13 && numero.toString().length() <= 19)
                .orElseThrow(() -> new ModelException(CartaoErrors.INVALID_NUMBER_CARD));
        cartaoEntity.setId(id);
        try {
            SaldoEntity saldoEntity = Optional.ofNullable(cartao.getSaldo()).map(saldo -> saldoRepository.findById(saldo.getId()).orElse(new SaldoEntity())).orElse(new SaldoEntity());
            cartaoEntity.setSaldo(saldoEntity);
            cartaoEntity.setNumeroCartao(cartaoEntity.getNumeroCartao());
            cartaoEntity.setSenha(cartaoEntity.getSenha());
            cartaoEntity.setStatus(Optional.ofNullable(cartaoEntity.getStatus()).orElse(CartaoStatus.ATIVO));
            cartaoEntity.setCreatedAt(cartao.getCreatedAt());
            cartaoEntity = cartaoRepository.save(cartaoEntity);
            return mapper.map(cartaoEntity, CartaoModel.class);
        } catch (RuntimeException e) {
            throw new ModelException(CartaoErrors.ERROR_CREATING, e);
        }
    }

    public String deleteCartaoById(Long id) {
        Optional.of(id).filter(cartaoRepository::existsById).orElseThrow(() -> new ModelException(CartaoErrors.NOT_FOUND));
        cartaoRepository.deleteById(id);
        return "Cartão excluído com sucesso.";
    }

    public boolean isCardExist(CartaoEntity cartaoEntity) {
        return cartaoRepository.findByNumeroCartao(cartaoEntity.getNumeroCartao()).isPresent();
    }
}
