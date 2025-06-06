package br.com.alura.AluraFake.infra;

import java.util.List;

public class RestErrorMessage {

    private String statusCode;
    private List<String> messages;

    public RestErrorMessage(String statusCode, List<String> messages) {
        this.statusCode = statusCode;
        this.messages = messages;
    }

    public RestErrorMessage(String statusCode, String message) {
        this.statusCode = statusCode;
        this.messages = List.of(message);
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public List<String> getMessages() {
        return this.messages;
    }
}
