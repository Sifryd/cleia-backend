package fr.cleia.sia.web.errors;

import fr.cleia.sia.domain.description.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final View error;

    public ApiExceptionHandler(View error) {
        this.error = error;
    }

    record ErreurDTO(String message) {}
    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<ErreurDTO> illegal(IllegalStateException e) {
        return ResponseEntity.unprocessableEntity().body(new ErreurDTO(e.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErreurDTO> invalid(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(new ErreurDTO("Requête invalide"));
    }
    @ExceptionHandler(DomainException.class)
    ResponseEntity<?> handleDomain(DomainException e) {
        HttpStatus status = switch (e.error()) {
            case NODE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case PARENT_REFUSE_DOSSIER, PARENT_REFUSE_PIECE, MOVE_DESC_FORBIDDEN, MOVE_SELF_FORBIDDEN ->
                    HttpStatus.BAD_REQUEST;
            case MOVE_FONDS_FORBIDDEN, DELETE_HAS_CHILDREN -> HttpStatus.CONFLICT;
        };
        var body = Map.of(
                "error", e.error().name(),
                "message", e.getMessage(),
                "context", e.context()
        );

        return ResponseEntity.status(status).body(body);
    }
}
