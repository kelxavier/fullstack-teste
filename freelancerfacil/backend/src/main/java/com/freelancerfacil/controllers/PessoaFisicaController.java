package com.freelancerfacil.controllers;

import java.net.URI;
import java.util.List;

import com.freelancerfacil.entities.PessoaFisica;
import com.freelancerfacil.services.PessoaFisicaService;
import com.freelancerfacil.services.exceptions.ApiCepException;
import com.freelancerfacil.services.exceptions.FornecedorInvalidoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/api/fornecedorpf")
    public class PessoaFisicaController {

    @Autowired
    private PessoaFisicaService fisicaService;

    @CrossOrigin(value = "http://localhost:3000")
    @PostMapping(value = "/validarCEP")
    public ResponseEntity<PessoaFisica> procurar(@RequestBody PessoaFisica cep) {

        try {
            return ResponseEntity.ok().body(fisicaService.validarCEP(cep));
        }
        catch(ApiCepException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping(value = "/")
    public ResponseEntity<List<PessoaFisica>> buscarTodos() {
        return ResponseEntity.ok().body(fisicaService.fornecedores());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PessoaFisica> buscarPorId(@PathVariable Long id) {
        PessoaFisica fornecedor = fisicaService.buscarPorId(id);

        return ResponseEntity.ok().body(fornecedor);
    }

    @PostMapping(value = "/cadastro")
    @CrossOrigin(value = "http://localhost:3000")
    public ResponseEntity<PessoaFisica> cadastro(@RequestBody PessoaFisica pessoaFisica) {

        try {

            PessoaFisica obj = fisicaService.cadastro(pessoaFisica);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(obj.getId()).toUri();
            return ResponseEntity.created(uri).body(obj);

        }
        catch(FornecedorInvalidoException f) {

            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/atualizar/{id}")
    public ResponseEntity<PessoaFisica> atualizar(@PathVariable Long id, @RequestBody PessoaFisica fornecedor) {
        return ResponseEntity.ok().body(fisicaService.atualizar(id, fornecedor));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<PessoaFisica> delete(@PathVariable Long id) {
        fisicaService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/cadastro/empresa/{id}")
    public ResponseEntity<Void> cadastroFornecedorPF(@RequestBody PessoaFisica fornecedor, @PathVariable Long id) {

        fisicaService.cadastroEmpresa(fornecedor, id);

        return ResponseEntity.noContent().build();

    }

}
