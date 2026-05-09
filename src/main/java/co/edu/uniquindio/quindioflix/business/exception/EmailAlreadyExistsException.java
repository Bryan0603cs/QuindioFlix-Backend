package co.edu.uniquindio.quindioflix.business.exception;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String email) {
        super("EMAIL_ALREADY_EXISTS", "Ya existe un usuario con email: "+email);

    }

}
