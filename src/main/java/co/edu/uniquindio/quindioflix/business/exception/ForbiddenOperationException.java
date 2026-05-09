package co.edu.uniquindio.quindioflix.business.exception;

public class ForbiddenOperationException extends DomainException {

    public ForbiddenOperationException(String message) {
        super("FORBIDDEN_OPERATION", message);
    }
}
