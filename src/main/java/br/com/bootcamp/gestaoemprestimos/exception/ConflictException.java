package br.com.bootcamp.gestaoemprestimos.exception;

public class ConflictException extends RuntimeException{
    public ConflictException(String mensagem){
        super(mensagem);
    }
}
