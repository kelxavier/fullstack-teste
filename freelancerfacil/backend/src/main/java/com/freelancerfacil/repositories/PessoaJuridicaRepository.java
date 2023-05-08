package com.freelancerfacil.repositories;

import java.util.Optional;

import com.freelancerfacil.entities.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long> {

    Boolean existsByCNPJ(String cnpj);

    Optional<PessoaJuridica> findByCNPJ(String cnpj);
}
