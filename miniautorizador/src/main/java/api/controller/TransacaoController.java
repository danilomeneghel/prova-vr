package api.controller;

import api.entity.TransacaoEntity;
import api.service.TransacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping( "/transacoes" )
@Tag( name = "Transações", description = "Cadastro de Transações" )
@Validated
public class TransacaoController {

    @Autowired
    TransacaoService transacaoService;

    @GetMapping( value = "/{id}" )
    public ResponseEntity< TransacaoEntity > getTransactionById(@PathVariable Long id) {
        return new ResponseEntity< >(transacaoService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity< Iterable<TransacaoEntity> > listTransactions() {
        return new ResponseEntity< >( transacaoService.findAll(), HttpStatus.OK );
    }

    @PostMapping
    public ResponseEntity< TransacaoEntity > createTransaction( @Valid @RequestBody TransacaoEntity transacaoEntity) {
        return new ResponseEntity< >(transacaoService.save(transacaoEntity), HttpStatus.CREATED );
    }

    @DeleteMapping( value = "/{id}" )
    public ResponseEntity< String > deleteTransaction( @PathVariable( "id" ) Long id ) {
        return new ResponseEntity< >( transacaoService.deleteById( id ), HttpStatus.OK );
    }

}