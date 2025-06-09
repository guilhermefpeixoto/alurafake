package br.com.alura.AluraFake.exceptions;

public class UserNotInstructorException extends RuntimeException {
    public UserNotInstructorException(String message) {
        super(message);
    }
}
