package api.controller;

import api.entity.CartaoEntity;
import api.enums.CartaoStatus;
import api.model.CartaoModel;
import api.model.CriaCartaoModel;
import api.service.CartaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping( "/cartoes" )
@Tag( name = "Cartões", description = "Cadastro de Cartões" )
@Validated
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @GetMapping( value = "/{numeroCartao}" )
    public ResponseEntity< BigDecimal > getCardByNumber(@PathVariable String numeroCartao) {
        return new ResponseEntity< >(cartaoService.findCartaoByNumeroCartao(numeroCartao), HttpStatus.OK);
    }

    @GetMapping( value = "/id/{id}" )
    public ResponseEntity< CartaoModel > getCardByNumber(@PathVariable Long id) {
        return new ResponseEntity< >(cartaoService.findCartaoById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity< List< CartaoModel > > listCards() {
        return new ResponseEntity< >( cartaoService.findAllByOrderByNumeroCartaoAsc(), HttpStatus.OK );
    }

    @GetMapping( value = "/status/{status}" )
    public ResponseEntity< List< CartaoModel > > listCardsByStatus( @PathVariable( "status" ) String status ) {
        return new ResponseEntity< >( cartaoService.findAllByStatusOrderByNumeroCartaoAsc( CartaoStatus.fromValue( status ) ), HttpStatus.OK );
    }

    @PostMapping
    public ResponseEntity< CartaoModel > createCard( @Valid @RequestBody CriaCartaoModel criaCartaoModel) {
        return new ResponseEntity< >(cartaoService.save(criaCartaoModel), HttpStatus.CREATED );
    }

    @PutMapping( value = "/{id}" )
    public ResponseEntity< CartaoModel > updateCard( @PathVariable( "id" ) Long id, @Valid @RequestBody CartaoEntity cartaoEntity) {
        return new ResponseEntity< >(cartaoService.update( id, cartaoEntity), HttpStatus.OK );
    }

    @DeleteMapping( value = "/{id}" )
    public ResponseEntity< String > deleteCard( @PathVariable( "id" ) Long id ) {
        return new ResponseEntity< >( cartaoService.deleteCartaoById( id ), HttpStatus.OK );
    }

}