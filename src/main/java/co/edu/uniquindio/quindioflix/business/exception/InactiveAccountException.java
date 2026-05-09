package co.edu.uniquindio.quindioflix.business.exception;

public class InactiveAccountException extends DomainException {
    public InactiveAccountException(Long id) {
        super("INACTIVE_ACCOUNT", "La cuenta del usuario "+id+" no está activa.");

    }

}
