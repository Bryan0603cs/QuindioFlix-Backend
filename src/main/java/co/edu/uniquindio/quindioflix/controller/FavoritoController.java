package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.command.AgregarFavoritoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoResponse;
import co.edu.uniquindio.quindioflix.business.service.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService service;

    @GetMapping("/perfil/{perfilId}")
    @Operation(summary = "Listar favoritos por perfil", description = "Devuelve favoritos paginados de un perfil autorizado.")
    @PreAuthorize("@authorizationService.canAccessPerfil(#perfilId)")
    public Page<ContenidoResponse> listarPorPerfil(@PathVariable Long perfilId, Pageable pageable) {
        return service.listarPorPerfil(perfilId, pageable);
    }

    @PostMapping
    @Operation(summary = "Agregar favorito")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@authorizationService.canAccessPerfil(#command.perfilId())")
    public Map<String, Long> agregar(@Valid @RequestBody AgregarFavoritoCommand command) {
        return Map.of("idFavorito", service.agregar(command));
    }

    @DeleteMapping("/{perfilId}/{contenidoId}")
    @Operation(summary = "Eliminar favorito")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@authorizationService.canAccessPerfil(#perfilId)")
    public void eliminar(@PathVariable Long perfilId, @PathVariable Long contenidoId) {
        service.eliminar(perfilId, contenidoId);
    }
}
