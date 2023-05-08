package com.freelancerfacil.repositories;

import java.util.Optional;

import com.freelancerfacil.entities.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {

    Boolean existsByCpf(String cpf);

    Optional<PessoaFisica> findByCpf(String cpf);

}
