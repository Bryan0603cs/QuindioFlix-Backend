package co.edu.uniquindio.quindioflix.business.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    ResponseEntity<ErrorResponse> handleDomainException(DomainException exception, HttpServletRequest request) {
        HttpStatus status = switch (exception.code()) {
            case "RESOURCE_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "EMAIL_ALREADY_EXISTS", "DUPLICATED_RATING", "DUPLICATED_FAVORITE" -> HttpStatus.CONFLICT;
            case "INACTIVE_ACCOUNT", "CHILD_PROFILE_RESTRICTION", "USER_NOT_MODERATOR", "FORBIDDEN_OPERATION" -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.BAD_REQUEST;
        };

        return build(exception.code(), exception.getMessage(), status, request, List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<ErrorResponse.FieldDetail> details = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponse.FieldDetail(error.getField(), error.getDefaultMessage()))
                .toList();

        return build(
                "VALIDATION_ERROR",
                "Hay campos inválidos en la petición.",
                HttpStatus.BAD_REQUEST,
                request,
                details
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException exception,
            HttpServletRequest request
    ) {
        return build(
                "BAD_CREDENTIALS",
                "Email o contraseña incorrectos.",
                HttpStatus.UNAUTHORIZED,
                request,
                List.of()
        );
    }



    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ErrorResponse> handleMissingParameter(
            MissingServletRequestParameterException exception,
            HttpServletRequest request
    ) {
        return build(
                "MISSING_REQUEST_PARAMETER",
                "Falta el parámetro requerido: " + exception.getParameterName(),
                HttpStatus.BAD_REQUEST,
                request,
                List.of(new ErrorResponse.FieldDetail(exception.getParameterName(), "Parámetro requerido no enviado"))
        );
    }

    @ExceptionHandler({DisabledException.class, LockedException.class})
    ResponseEntity<ErrorResponse> handleDisabledAccount(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        return build(
                "ACCOUNT_NOT_ACTIVE",
                "La cuenta no está activa o se encuentra suspendida.",
                HttpStatus.FORBIDDEN,
                request,
                List.of()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException exception,
            HttpServletRequest request
    ) {
        return build(
                "ACCESS_DENIED",
                "No tiene permisos para ejecutar esta operación.",
                HttpStatus.FORBIDDEN,
                request,
                List.of()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {
        log.warn("Restricción de integridad violada en {}", request.getRequestURI(), exception);
        return build(
                "DATA_INTEGRITY_ERROR",
                "La operación rompe una restricción de integridad.",
                HttpStatus.CONFLICT,
                request,
                List.of()
        );
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception, HttpServletRequest request) {
        log.error("Error inesperado en {}", request.getRequestURI(), exception);
        return build(
                "INTERNAL_ERROR",
                "Error interno. Revise logs del backend.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request,
                List.of()
        );
    }

    private ResponseEntity<ErrorResponse> build(
            String code,
            String message,
            HttpStatus status,
            HttpServletRequest request,
            List<ErrorResponse.FieldDetail> details
    ) {
        ErrorResponse response = new ErrorResponse(
                code,
                message,
                status.value(),
                request.getRequestURI(),
                LocalDateTime.now(),
                details
        );

        return ResponseEntity.status(status).body(response);
    }
}
