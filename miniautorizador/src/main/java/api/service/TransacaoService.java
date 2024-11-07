package api.service;

import api.entity.CartaoEntity;
import api.entity.SaldoEntity;
import api.entity.TransacaoEntity;
import api.enums.CartaoStatus;
import api.exception.ModelException;
import api.model.CriaTransacaoModel;
import api.model.TransacaoModel;
import api.model.errors.TransacaoErrors;
import api.repository.CartaoRepository;
import api.repository.SaldoRepository;
import api.repository.TransacaoRepository;
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
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    private ModelMapper mapper = new ModelMapper();

    public TransacaoModel findById(Long id) {
        TransacaoEntity transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new ModelException(TransacaoErrors.NOT_FOUND));
        return mapper.map(transacao, TransacaoModel.class);
    }

    public List<TransacaoModel> findAll() {
        List<TransacaoEntity> transacoes = transacaoRepository.findAll();
        Optional.ofNullable(transacoes)
                .filter(transacoesList -> !transacoesList.isEmpty())
                .orElseThrow(() -> new ModelException(TransacaoErrors.NOT_FOUND));
        return transacoes.stream().map(entity -> mapper.map(entity, TransacaoModel.class)).collect(Collectors.toList());
    }

    public String save(CriaTransacaoModel criaTransacaoModel) {
        Optional<CartaoEntity> cartao = cartaoRepository.findByNumeroCartao(criaTransacaoModel.getNumeroCartao());
        while (!cartao.isEmpty()) {
            while (cartao.get().getStatus().equals(CartaoStatus.ATIVO)) {
                while (cartao.get().getSenha().equals(criaTransacaoModel.getSenha())) {
                    while (cartao.get().getSaldo().getValor().compareTo(criaTransacaoModel.getValor()) >= 0) {
                        updateBalance(cartao, criaTransacaoModel.getValor(), "debito");
                        TransacaoEntity transacaoEntity = mapper.map(criaTransacaoModel, TransacaoEntity.class);
                        transacaoEntity.setCartao(cartao.get());
                        transacaoRepository.save(transacaoEntity);
                        return "OK";
                    }
                    throw new ModelException(TransacaoErrors.INSUFFICIENT_BALANCE);
                }
                throw new ModelException(TransacaoErrors.INVALID_PASSWORD);
            }
            throw new ModelException(TransacaoErrors.INATIVE_CARD);
        }
        throw new ModelException(TransacaoErrors.INVALID_NUMBER_CARD);
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
                            Optional<CartaoEntity> cartao = cartaoRepository.findByNumeroCartao(transacaoEntity.getCartao().getNumeroCartao());
                            transacaoRepository.deleteById(id);
                            updateBalance(cartao, transacaoEntity.getValor(), "credito");
                        }, () -> {
                            throw new ModelException(TransacaoErrors.NOT_FOUND);
                        }
                );
        return "Transação excluída com sucesso.";
    }

}