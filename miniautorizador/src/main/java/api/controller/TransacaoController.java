package api.controller;

import api.model.CriaTransacaoModel;
import api.model.TransacaoModel;
import api.service.TransacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping( "/transacoes" )
@Tag( name = "Transações", description = "Cadastro de Transações" )
@Validated
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @GetMapping( value = "/{id}" )
    public ResponseEntity< TransacaoModel > getTransactionById(@PathVariable Long id) {
        return new ResponseEntity< >(transacaoService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity< List< TransacaoModel > > listTransactions() {
        return new ResponseEntity< >( transacaoService.findAll(), HttpStatus.OK );
    }

    @PostMapping
    public ResponseEntity< String > createTransaction(@Valid @RequestBody CriaTransacaoModel criaTransacaoModel) {
        return new ResponseEntity< >(transacaoService.save(criaTransacaoModel), HttpStatus.CREATED );
    }

    @DeleteMapping( value = "/{id}" )
    public ResponseEntity< String > deleteTransaction( @PathVariable( "id" ) Long id ) {
        return new ResponseEntity< >( transacaoService.deleteById( id ), HttpStatus.OK );
    }

}