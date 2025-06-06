package br.com.alura.AluraFake.exceptions;

public class EmailAlreadyRegisteredException extends Exception {
    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}