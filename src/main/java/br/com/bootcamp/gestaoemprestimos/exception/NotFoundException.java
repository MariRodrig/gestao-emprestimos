package br.com.bootcamp.gestaoemprestimos.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String mensagem){
        super(mensagem);
    }
}
