package api.service;

import api.entity.CartaoEntity;
import api.entity.SaldoEntity;
import api.entity.TransacaoEntity;
import api.enums.CartaoStatus;
import api.exception.BadRequestException;
import api.exception.NotFoundException;
import api.model.TransacaoModel;
import api.model.errors.CartaoErrors;
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

    public TransacaoModel findById(Long id ) {
        TransacaoEntity transacao = transacaoRepository.findById( id ).orElse( new TransacaoEntity() );
        while ( transacao.getId() == null ) {
            throw new NotFoundException(TransacaoErrors.NOT_FOUND);
        }
        return mapper.map(transacao, TransacaoModel.class);
    }

    public List<TransacaoModel> findAll() {
        List<TransacaoEntity> transacoes = transacaoRepository.findAll();
        while ( transacoes.isEmpty() ) {
            throw new NotFoundException(TransacaoErrors.NOT_FOUND);
        }
        return transacoes.stream().map(entity -> mapper.map(entity, TransacaoModel.class)).collect(Collectors.toList());
    }

    public TransacaoModel save(TransacaoEntity transacaoEntity) {
        Optional<CartaoEntity> cartao = cartaoRepository.findByNumeroCartao(transacaoEntity.getCartao().getNumeroCartao());
        while (!cartao.isEmpty()) {
            while (cartao.get().getStatus().equals(CartaoStatus.ATIVO)) {
                while (cartao.get().getSenha().equals(transacaoEntity.getCartao().getSenha())) {
                    while (cartao.get().getSaldo().getValor().compareTo(transacaoEntity.getValor()) >= 0) {
                        transacaoEntity.setCartao(cartao.get());
                        updateBalance(transacaoEntity, cartao);
                        transacaoEntity = transacaoRepository.save(transacaoEntity);
                        return mapper.map(transacaoEntity, TransacaoModel.class);
                    }
                    throw new BadRequestException(TransacaoErrors.INSUFFICIENT_BALANCE);
                }
                throw new BadRequestException(TransacaoErrors.INVALID_PASSWORD);
            }
            throw new BadRequestException(TransacaoErrors.INATIVE_CARD);
        }
        throw new NotFoundException(TransacaoErrors.INVALID_NUMBER_CARD);
    }

    public SaldoEntity updateBalance(TransacaoEntity transacaoEntity, Optional<CartaoEntity> cartao) {
        Optional<SaldoEntity> saldoEntity = saldoRepository.findById(cartao.get().getSaldo().getId());
        BigDecimal novoValor = saldoEntity.get().getValor().subtract(transacaoEntity.getValor());
        saldoEntity.get().setValor(novoValor);
        return saldoRepository.save(saldoEntity.get());
    }

    public String deleteById(Long id ) {
        while ( transacaoRepository.existsById( id ) ) {
            transacaoRepository.deleteById( id );
            return "Transação excluída com sucesso.";
        }
        throw new NotFoundException(TransacaoErrors.NOT_FOUND);
    }

}