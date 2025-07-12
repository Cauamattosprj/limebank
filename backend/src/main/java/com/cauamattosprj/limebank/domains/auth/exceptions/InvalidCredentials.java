package com.cauamattosprj.limebank.domains.auth.exceptions;

public class InvalidCredentials extends RuntimeException {
    public InvalidCredentials(String message) {
        super(message);
    };

    public InvalidCredentials() {
        super("Credenciais inválidas");
    }
}
