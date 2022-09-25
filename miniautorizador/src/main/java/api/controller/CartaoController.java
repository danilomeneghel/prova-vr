package api.controller;

import api.entity.CartaoEntity;
import api.enums.CartaoStatus;
import api.exception.CustomErrorType;
import api.exception.RecordNotFoundException;
import api.model.CartaoModel;
import api.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping( "/cartoes" )
@Validated
public class CartaoController {

    @Autowired
    CartaoService cartaoService;

    @GetMapping( value = "/{numeroCartao}" )
    public ResponseEntity< ? > getCardByNumber(@PathVariable String numeroCartao) {
        CartaoModel cartao = cartaoService.findByNumeroCartao(numeroCartao);
        if( cartao.getNumeroCartao() == null ) {
            return new ResponseEntity< >( new CustomErrorType( "Número do cartão " + numeroCartao + " não encontrado." ), HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity< >(cartao, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity< ? > listCards() {
        List< CartaoModel > cartoes = cartaoService.findAllByOrderByNumeroCartaoAsc();
        if( cartoes.isEmpty() ) {
            return new ResponseEntity< >( new CustomErrorType( "Nenhum cartão cadastrado." ), HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity< >( cartoes, HttpStatus.OK );
    }

    @GetMapping( value = "/status/{status}" )
    public ResponseEntity< ? > listCardsByStatus( @PathVariable( "status" ) String status ) {
        List< CartaoModel > cartoes = cartaoService.findAllByStatusOrderByNumeroCartaoAsc( CartaoStatus.fromValue( status ) );
        if( cartoes.isEmpty() ) {
            return new ResponseEntity< >( new CustomErrorType( "Nenhum cartão ativo." ), HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity< >( cartoes, HttpStatus.OK );
    }

    @PostMapping
    public ResponseEntity< ? > createCard( @Valid @RequestBody CartaoEntity cartaoEntity) throws RecordNotFoundException {
        if( cartaoService.isCartaoExist(cartaoEntity) ) {
            return new ResponseEntity< >( new CustomErrorType( "Número do cartão " + cartaoEntity.getNumeroCartao() + " já cadastrado." ), HttpStatus.CONFLICT );
        }
        CartaoModel cartaoEntityNovo = cartaoService.save(cartaoEntity);
        return new ResponseEntity< >(cartaoEntityNovo, HttpStatus.CREATED );
    }

    @PutMapping( value = "/{id}" )
    public ResponseEntity< ? > updateCard( @PathVariable( "id" ) Long id, @Valid @RequestBody CartaoEntity cartaoEntity) throws RecordNotFoundException {
        if( cartaoService.findCartaoById( id ).getId() == null ) {
            return new ResponseEntity< >( new CustomErrorType( "Cartão com id " + id + " não encontrado." ), HttpStatus.NOT_FOUND );
        }
        CartaoModel cartaoEntityAlterado = cartaoService.update( id, cartaoEntity);
        return new ResponseEntity< >(cartaoEntityAlterado, HttpStatus.OK );
    }

    @DeleteMapping( value = "/{id}" )
    public ResponseEntity< ? > deleteCard( @PathVariable( "id" ) Long id ) throws RecordNotFoundException {
        if( cartaoService.findCartaoById( id ).getId() == null ) {
            return new ResponseEntity< >( new CustomErrorType( "Cartão com id " + id + " não encontrado." ), HttpStatus.NOT_FOUND );
        }
        String cartaoExcluido = cartaoService.deleteCartaoById( id );
        return new ResponseEntity< >( cartaoExcluido, HttpStatus.OK );
    }

}