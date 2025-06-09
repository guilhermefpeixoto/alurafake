package br.com.alura.AluraFake.exceptions;

public class WrongNumberOfCorrectOptionsException extends RuntimeException {
    public WrongNumberOfCorrectOptionsException(String message) {
        super(message);
    }
}
