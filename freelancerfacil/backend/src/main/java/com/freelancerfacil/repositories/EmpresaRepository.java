package com.freelancerfacil.repositories;

import java.util.Optional;

import com.freelancerfacil.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {


    Boolean existsByCNPJ(String cnpj);

    Optional<Empresa> findByCNPJ(String cnpj);
}
