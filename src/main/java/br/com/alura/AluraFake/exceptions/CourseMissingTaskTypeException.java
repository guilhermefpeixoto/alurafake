package br.com.alura.AluraFake.exceptions;

public class CourseMissingTaskTypeException extends RuntimeException {
    public CourseMissingTaskTypeException(String message) {
        super(message);
    }
}
