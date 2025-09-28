package fr.cleia.sia.web.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    record ErreurDTO(String message) {}
    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<ErreurDTO> illegal(IllegalStateException e) {
        return ResponseEntity.unprocessableEntity().body(new ErreurDTO(e.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErreurDTO> invalid(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(new ErreurDTO("Requête invalide"));
    }
}
