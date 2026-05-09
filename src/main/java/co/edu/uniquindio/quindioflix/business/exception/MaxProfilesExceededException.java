package co.edu.uniquindio.quindioflix.business.exception;

public class MaxProfilesExceededException extends DomainException {
    public MaxProfilesExceededException(int max) {
        super("MAX_PROFILES_EXCEEDED", "El plan permite máximo "+max+" perfiles.");

    }

}
