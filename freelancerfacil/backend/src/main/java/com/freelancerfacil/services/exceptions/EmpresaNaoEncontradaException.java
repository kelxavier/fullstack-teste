package com.freelancerfacil.services.exceptions;

public class EmpresaNaoEncontradaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmpresaNaoEncontradaException(String msg) {
        super(msg);
    }

}
