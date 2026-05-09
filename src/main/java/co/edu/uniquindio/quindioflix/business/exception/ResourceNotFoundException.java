package co.edu.uniquindio.quindioflix.business.exception;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String resource, Object id) {
        super("RESOURCE_NOT_FOUND", resource+" no encontrado: "+id);

    }

}
