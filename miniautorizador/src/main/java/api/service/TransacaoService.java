package api.service;

import api.entity.CartaoEntity;
import api.entity.SaldoEntity;
import api.entity.TransacaoEntity;
import api.enums.CartaoStatus;
import api.exception.ModelException;
import api.exception.TransacaoException;
import api.model.CartaoModel;
import api.model.CriaTransacaoModel;
import api.model.TransacaoModel;
import api.model.errors.TransacaoErrors;
import api.repository.CartaoRepository;
import api.repository.SaldoRepository;
import api.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    public TransacaoModel findById(Long id) {
        TransacaoEntity transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new ModelException(TransacaoErrors.NOT_FOUND));
        return toModel(transacao);
    }

    public List<TransacaoModel> findAll() {
        List<TransacaoEntity> transacoes = transacaoRepository.findAll();
        Optional.ofNullable(transacoes)
                .filter(transacoesList -> !transacoesList.isEmpty())
                .orElseThrow(() -> new ModelException(TransacaoErrors.NOT_FOUND));
        return transacoes.stream().map(this::toModel).collect(Collectors.toList());
    }

    public String save(CriaTransacaoModel criaTransacaoModel) {
        Optional<CartaoEntity> cartao = cartaoRepository.findByNumeroCartao(criaTransacaoModel.getNumeroCartao());

        while (!cartao.isEmpty()) {
            while (cartao.get().getStatus().equals(CartaoStatus.ATIVO)) {
                while (cartao.get().getSenha().equals(criaTransacaoModel.getSenhaCartao())) {
                    while (cartao.get().getSaldo().getValor().compareTo(criaTransacaoModel.getValor()) >= 0) {
                        updateBalance(cartao, criaTransacaoModel.getValor(), "debito");
                        TransacaoEntity transacaoEntity = new TransacaoEntity();
                        transacaoEntity.setCartao(cartao.get());
                        transacaoEntity.setValor(criaTransacaoModel.getValor());
                        transacaoRepository.save(transacaoEntity);
                        return "OK";
                    }
                    throw new TransacaoException("SALDO_INSUFICIENTE");
                }
                throw new TransacaoException("SENHA_INVALIDA");
            }
            throw new TransacaoException("CARTAO_INEXISTENTE");
        }
        throw new TransacaoException("CARTAO_INEXISTENTE");
    }

    public SaldoEntity updateBalance(Optional<CartaoEntity> cartao, BigDecimal valorTransacao, String tipo) {
        CartaoEntity cartaoEntity = cartao.orElseThrow(() -> new ModelException(TransacaoErrors.INVALID_NUMBER_CARD));
        SaldoEntity saldoEntity = cartaoEntity.getSaldo();
        Optional.ofNullable(saldoEntity).orElseThrow(() -> new ModelException(TransacaoErrors.NOT_FOUND));
        BigDecimal novoValor = tipo.equals("debito")
                ? saldoEntity.getValor().subtract(valorTransacao)
                : saldoEntity.getValor().add(valorTransacao);
        saldoEntity.setValor(novoValor);
        return saldoRepository.save(saldoEntity);
    }

    public String deleteById(Long id) {
        transacaoRepository.findById(id)
                .ifPresentOrElse(
                        transacaoEntity -> {
                            Optional<CartaoEntity> cartao = cartaoRepository.findByNumeroCartao(
                                    transacaoEntity.getCartao().getNumeroCartao());
                            transacaoRepository.deleteById(id);
                            updateBalance(cartao, transacaoEntity.getValor(), "credito");
                        }, () -> {
                            throw new ModelException(TransacaoErrors.NOT_FOUND);
                        }
                );
        return "Transação excluída com sucesso.";
    }

    private TransacaoModel toModel(TransacaoEntity entity) {
        CartaoModel cartaoModel = null;
        if (entity.getCartao() != null) {
            SaldoEntity saldo = entity.getCartao().getSaldo();
            cartaoModel = CartaoModel.builder()
                    .id(entity.getCartao().getId())
                    .numeroCartao(entity.getCartao().getNumeroCartao())
                    .saldo(saldo != null ? saldo.getValor() : null)
                    .status(entity.getCartao().getStatus())
                    .build();
        }
        return TransacaoModel.builder()
                .id(entity.getId())
                .cartao(cartaoModel)
                .valor(entity.getValor())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

}