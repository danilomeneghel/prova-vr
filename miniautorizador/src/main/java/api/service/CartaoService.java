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
import java.util.stream.Collectors;

@Service
@Transactional
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    private ModelMapper mapper = new ModelMapper();

    public BigDecimal findCartaoByNumeroCartao(String numeroCartao ) {
        CartaoEntity cartao = cartaoRepository.findByNumeroCartao(numeroCartao).orElse( null );
        while ( cartao == null ) {
            throw new ModelException(CartaoErrors.INVALID_NUMBER_CARD);
        }
        return cartao.getSaldo().getValor();
    }

    public CartaoModel findCartaoById( Long id ) {
        CartaoEntity cartao = cartaoRepository.findById( id ).orElse( new CartaoEntity() );
        while ( cartao.getId() == null ) {
            throw new ModelException(CartaoErrors.NOT_FOUND);
        }
        return mapper.map(cartao, CartaoModel.class);
    }

    public List< CartaoModel > findAllByOrderByNumeroCartaoAsc() {
        List<CartaoEntity> cartoes = cartaoRepository.findAllByOrderByNumeroCartaoAsc();
        while ( cartoes.isEmpty() ) {
            throw new ModelException(CartaoErrors.NOT_FOUND);
        }
        return cartoes.stream().map(entity -> mapper.map(entity, CartaoModel.class)).collect(Collectors.toList());
    }

    public List< CartaoModel > findAllByStatusOrderByNumeroCartaoAsc( CartaoStatus status ) {
        List<CartaoEntity> cartoes = cartaoRepository.findAllByStatusOrderByNumeroCartaoAsc( status );
        while ( cartoes.isEmpty() ) {
            throw new ModelException(CartaoErrors.NOT_FOUND);
        }
        return cartoes.stream().map(entity -> mapper.map(entity, CartaoModel.class)).collect(Collectors.toList());
    }

    public CartaoModel save(CriaCartaoModel criaCartaoModel) {
        CartaoEntity cartaoEntity = mapper.map(criaCartaoModel, CartaoEntity.class);
        while ( isCardExist(cartaoEntity) ) {
            throw new ModelException(CartaoErrors.CARD_EXISTS);
        }
        try {
            SaldoEntity saldoEntity = new SaldoEntity();
            saldoRepository.save(saldoEntity);
            cartaoEntity.setSaldo(saldoEntity);
            cartaoEntity.setStatus(CartaoStatus.ATIVO);
            cartaoEntity = cartaoRepository.save(cartaoEntity);
            return mapper.map(cartaoEntity, CartaoModel.class);
        } catch (Exception e) {
            throw new ModelException(CartaoErrors.ERROR_CREATING);
        }
    }

    public CartaoModel update(Long id, CartaoEntity cartaoEntity) {
        CartaoEntity cartao = cartaoRepository.findById( id ).orElse( new CartaoEntity() );
        while ( cartaoRepository.existsById( id ) ) {
            cartaoEntity.setId(id);
            SaldoEntity saldoEntity = (cartao.getSaldo() != null) ? saldoRepository.findById( cartao.getSaldo().getId() ).orElse( new SaldoEntity() ) : new SaldoEntity();
            cartaoEntity.setSaldo(saldoEntity);
            cartaoEntity.setNumeroCartao( cartaoEntity.getNumeroCartao() );
            cartaoEntity.setSenha( cartaoEntity.getSenha() );
            cartaoEntity.setStatus( (cartaoEntity.getStatus() != null) ? cartaoEntity.getStatus() : CartaoStatus.ATIVO );
            cartaoEntity.setCreatedAt(cartao.getCreatedAt());
            cartaoEntity = cartaoRepository.save(cartaoEntity);
            return mapper.map(cartaoEntity, CartaoModel.class);
        }
        throw new ModelException(CartaoErrors.NOT_FOUND);
    }

    public String deleteCartaoById( Long id ) {
        while ( cartaoRepository.existsById( id ) ) {
            cartaoRepository.deleteById( id );
            return "Cartão excluído com sucesso.";
        }
        throw new ModelException(CartaoErrors.NOT_FOUND);
    }

    public boolean isCardExist( CartaoEntity cartaoEntity) {
        return cartaoRepository.findByNumeroCartao( cartaoEntity.getNumeroCartao() ).isPresent();
    }

}