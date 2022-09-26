package api.service;

import api.entity.CartaoEntity;
import api.entity.TransacaoEntity;
import api.enums.CartaoStatus;
import api.exception.ModelException;
import api.model.errors.CartaoErrors;
import api.model.errors.TransacaoErrors;
import api.repository.CartaoRepository;
import api.repository.TransacaoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    private ModelMapper mapper = new ModelMapper();

    public TransacaoEntity findById(Long id ) {
        TransacaoEntity transacao = transacaoRepository.findById( id ).orElse( new TransacaoEntity() );
        while ( transacao.getId() == null ) {
            throw new ModelException(CartaoErrors.NOT_FOUND);
        }
        return transacao;
    }

    public Iterable<TransacaoEntity> findAll() {
        Iterable<TransacaoEntity> transacoes = transacaoRepository.findAll();
        while ( transacoes.iterator().next().getId() == null ) {
            throw new ModelException(TransacaoErrors.NOT_FOUND);
        }
        return transacoes;
    }

    public TransacaoEntity save(TransacaoEntity transacaoEntity) {
        Optional<CartaoEntity> cartao = cartaoRepository.findByNumeroCartao( transacaoEntity.getCartao().getNumeroCartao() );
        while (!cartao.isEmpty()) {
            while (cartao.get().getStatus().equals(CartaoStatus.ATIVO)) {
                while (cartao.get().getSenha().equals(transacaoEntity.getCartao().getSenha())) {
                    while (cartao.get().getSaldo().getValor().compareTo(transacaoEntity.getValor()) >= 0) {
                        transacaoEntity.setCartao(cartao.get());
                        transacaoEntity.setValor(transacaoEntity.getValor());
                        return transacaoRepository.save(transacaoEntity);
                    }
                    throw new ModelException(TransacaoErrors.INSUFFICIENT_BALANCE);
                }
                throw new ModelException(TransacaoErrors.INVALID_PASSWORD);
            }
            throw new ModelException(TransacaoErrors.INATIVE_CARD);
        }
        throw new ModelException(TransacaoErrors.INVALID_NUMBER_CARD);
    }

    public String deleteById(Long id ) {
        while ( transacaoRepository.existsById( id ) ) {
            transacaoRepository.deleteById( id );
            return "Transação excluída com sucesso.";
        }
        throw new ModelException(TransacaoErrors.NOT_FOUND);
    }

}