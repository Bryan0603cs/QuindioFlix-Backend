package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.command.EditarPerfilCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.PerfilResponse;
import co.edu.uniquindio.quindioflix.business.service.PerfilService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/perfiles")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService service;

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar perfil")
    @PreAuthorize("@authorizationService.canAccessPerfil(#id)")
    public PerfilResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EditarPerfilCommand command
    ) {
        return service.actualizar(id, command);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar perfil", description = "Elimina un perfil individual y sus dependencias, sin permitir borrar el último perfil de una cuenta.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@authorizationService.canAccessPerfil(#id)")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
