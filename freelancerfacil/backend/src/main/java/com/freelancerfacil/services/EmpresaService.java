package com.freelancerfacil.services;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.freelancerfacil.entities.Empresa;
import com.freelancerfacil.entities.PessoaFisica;
import com.freelancerfacil.entities.PessoaJuridica;
import com.freelancerfacil.repositories.EmpresaRepository;
import com.freelancerfacil.repositories.PessoaFisicaRepository;
import com.freelancerfacil.repositories.PessoaJuridicaRepository;
import com.freelancerfacil.services.exceptions.ApiCepException;
import com.freelancerfacil.services.exceptions.EmpresaInvalidaException;
import com.freelancerfacil.services.exceptions.EmpresaNaoEncontradaException;
import com.freelancerfacil.services.exceptions.FornecedorInvalidoException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaDAO;

    @Autowired
    private PessoaFisicaRepository fisicaDAO;

    @Autowired
    private PessoaJuridicaRepository juridicaDAO;

    private final WebClient webClient;

    public EmpresaService(WebClient.Builder builder) {
        webClient = builder.baseUrl("http://cep.la/").build();
    }

    public Empresa validarCEP(Empresa cep) {

        Mono<Empresa> endereco = webClient
                .get()
                .uri(cep.getCep())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    return Mono.error(new ApiCepException("CEP não encontrado"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    return Mono.error(new ApiCepException("Erro inesperado"));
                })
                .bodyToMono(Empresa.class)
                .onErrorResume(error -> {
                    if(error instanceof ApiCepException) {
                        throw new ApiCepException("CEP Inválido");
                    }
                    else {
                        throw new ApiCepException("Erro inesperado");
                    }
                });


        return endereco.block();
    }

    public Empresa cadastro(Empresa empresa) {

        if(this.empresaDAO.existsByCNPJ(empresa.getCNPJ())) {
            throw new EmpresaInvalidaException("CNPJ já cadastrado");
        }

        try {

            Empresa endereco = validarCEP(empresa);

            empresa.setBairro(endereco.getBairro());
            empresa.setCidade(endereco.getCidade());
            empresa.setLogradouro(endereco.getLogradouro());
            empresa.setUf(endereco.getUf());
        }
        catch(ApiCepException e) {
            throw new EmpresaInvalidaException("CEP inválido");
        }

        return empresaDAO.save(empresa);
    }

    public List<Empresa> buscarTodos() {
        return empresaDAO.findAll();
    }

    public Empresa buscarPorId(Long id) {
        Optional<Empresa> empresa = empresaDAO.findById(id);

        return empresa.orElseThrow(() -> new EmpresaNaoEncontradaException("Empresa não encontrada"));
    }

    public void delete(Long id) {

        Optional<Empresa> empresa = empresaDAO.findById(id);

        empresaDAO.delete(empresa.get());
    }

    public Empresa atualizar(Long id, Empresa empresa) {

        Optional<Empresa> entity = empresaDAO.findById(id);
        atualizarDados(entity.get(), empresa);

        return empresaDAO.save(entity.get());
    }

    private void atualizarDados(Empresa entity, Empresa empresa) {

        entity.setComplemento(empresa.getComplemento());
        entity.setNomeFantasia(empresa.getNomeFantasia());
    }

    public void cadastroFornecedorPF(Empresa empresa, Long id) {

        Optional<PessoaFisica> fornecedor = fisicaDAO.findById(id);
        Optional<Empresa> empresaAux = empresaDAO.findByCNPJ(empresa.getCNPJ());

        // Verificar se empresa é do PR e o fornecedor é menor de idade
        ZoneId timeZone = ZoneId.systemDefault();
        LocalDate getNascimento = fornecedor.get().getNascimento()
                .toInstant().atZone(timeZone)
                .toLocalDate();
        int anoNascimento = getNascimento.getYear();

        Date anoAtual = new Date();
        SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");

        String anoAtualConvert = getYearFormat.format(anoAtual);

        int getAnoAtualInteiro = Integer.parseInt(anoAtualConvert);
        int idade = getAnoAtualInteiro - anoNascimento;

        if(empresaAux.get().getUf().toUpperCase() == "PR" || idade < 18) {
            throw new FornecedorInvalidoException("Empresa do Paraná, Fornecedor menor de idade");
        }
        // FIM

        fornecedor.get().getEmpresas().add(empresaAux.get());
        empresaAux.get().getPessoasFisica().add(fornecedor.get());

        fisicaDAO.save(fornecedor.get());
        empresaDAO.save(empresaAux.get());
    }

    public void cadastroFornecedorPJ(Empresa empresa, Long id) {

        Optional<PessoaJuridica> fornecedor = juridicaDAO.findById(id);
        Optional<Empresa> empresaAux = empresaDAO.findByCNPJ(empresa.getCNPJ());


        fornecedor.get().getEmpresas().add(empresaAux.get());
        empresaAux.get().getPessoasJuridica().add(fornecedor.get());

        juridicaDAO.save(fornecedor.get());
        empresaDAO.save(empresaAux.get());
    }

}

