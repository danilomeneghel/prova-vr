package api.service;

import api.entity.CartaoEntity;
import api.entity.SaldoEntity;
import api.enums.CartaoStatus;
import api.exception.RecordNotFoundException;
import api.model.CartaoModel;
import api.repository.CartaoRepository;
import api.repository.SaldoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public CartaoModel findByNumeroCartao( String numeroCartao ) {
        CartaoEntity cartaoEntity = cartaoRepository.findByNumeroCartao(numeroCartao).orElse( new CartaoEntity() );
        return mapper.map(cartaoEntity, CartaoModel.class);
    }

    public CartaoModel findCartaoById( Long id ) {
        CartaoEntity cartaoEntity = cartaoRepository.findById( id ).orElse( new CartaoEntity() );
        return mapper.map(cartaoEntity, CartaoModel.class);
    }

    public List< CartaoModel > findAllByOrderByNumeroCartaoAsc() {
        List<CartaoEntity> cartoes = cartaoRepository.findAllByOrderByNumeroCartaoAsc();
        List< CartaoModel > listaCartoes = cartoes.stream().map(entity -> mapper.map(entity, CartaoModel.class)).collect(Collectors.toList());
        return listaCartoes;
    }

    public List< CartaoModel > findAllByStatusOrderByNumeroCartaoAsc( CartaoStatus status ) {
        List<CartaoEntity> cartoes = cartaoRepository.findAllByStatusOrderByNumeroCartaoAsc( status );
        List< CartaoModel > listaCartoes = cartoes.stream().map(entity -> mapper.map(entity, CartaoModel.class)).collect(Collectors.toList());
        return listaCartoes;
    }

    public CartaoModel save(CartaoEntity cartaoEntity) throws RecordNotFoundException {
        try {
            SaldoEntity saldoEntity = new SaldoEntity();
            saldoRepository.save(saldoEntity);
            cartaoEntity.setSaldo(saldoEntity);
            cartaoEntity.setStatus(CartaoStatus.ATIVO);
            cartaoEntity = cartaoRepository.save(cartaoEntity);
            return mapper.map(cartaoEntity, CartaoModel.class);
        } catch (Exception e) {
            throw new RecordNotFoundException( "Erro ao criar novo cartão. Tente novamente." );
        }
    }

    public CartaoModel update(Long id, CartaoEntity cartaoEntity) throws RecordNotFoundException {
        CartaoEntity cartao = cartaoRepository.findById( id ).orElse( new CartaoEntity() );
        if( cartao.getId() != null ) {
            SaldoEntity saldoEntity = saldoRepository.findById( id ).orElse( new SaldoEntity() );
            cartaoEntity.setSaldo(saldoEntity);
            cartaoEntity.setId(id);
            cartaoEntity.setNumeroCartao( cartaoEntity.getNumeroCartao() );
            cartaoEntity.setSenha( cartaoEntity.getSenha() );
            cartaoEntity.setStatus( (cartaoEntity.getStatus() != null) ? cartaoEntity.getStatus() : CartaoStatus.ATIVO );
            cartaoEntity.setCreatedAt(cartao.getCreatedAt());
            cartaoEntity = cartaoRepository.save(cartaoEntity);
            return mapper.map(cartaoEntity, CartaoModel.class);
        }
        throw new RecordNotFoundException( "ID não encontrado." );
    }

    public String deleteCartaoById( Long id ) throws RecordNotFoundException {
        if( cartaoRepository.existsById( id ) ) {
            cartaoRepository.deleteById( id );
            return "Cartão excluído com sucesso.";
        }
        throw new RecordNotFoundException( "ID não encontrado." );
    }

    public boolean isCartaoExist( CartaoEntity cartaoEntity) {
        return cartaoRepository.findByNumeroCartao( cartaoEntity.getNumeroCartao() ).isPresent();
    }

}