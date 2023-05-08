package com.freelancerfacil.services.exceptions;

public class FornecedorNaoEncontradoException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public FornecedorNaoEncontradoException(String msg) {
        super(msg);
    }

}
