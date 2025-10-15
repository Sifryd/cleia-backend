package fr.cleia.sia.domain.description.exception;

import java.util.Map;

public class DomainException extends RuntimeException{
    private final DomainError error;
    private final Map<String, Object> context;

    public DomainException(DomainError error, String message) {
        super(message);
        this.error = error;
        this.context = Map.of();
    }

    public DomainException(DomainError error, String message, Map<String, Object> context) {
        super(message);
        this.error = error;
        this.context = context;
    }

    public DomainError error() {
        return error;
    }

    public Map<String, Object> context() {
        return context;
    }
}

