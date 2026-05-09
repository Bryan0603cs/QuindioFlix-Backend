package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.command.AgregarFavoritoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoResponse;
import co.edu.uniquindio.quindioflix.business.service.FavoritoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Favoritos")
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService service;

    @GetMapping("/perfil/{perfilId}")
    @PreAuthorize("@authorizationService.canAccessPerfil(#perfilId)")
    public List<ContenidoResponse> listarPorPerfil(@PathVariable Long perfilId) {
        return service.listarPorPerfil(perfilId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@authorizationService.canAccessPerfil(#command.perfilId())")
    public Map<String, Long> agregar(@Valid @RequestBody AgregarFavoritoCommand command) {
        return Map.of("idFavorito", service.agregar(command));
    }

    @DeleteMapping("/{perfilId}/{contenidoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@authorizationService.canAccessPerfil(#perfilId)")
    public void eliminar(@PathVariable Long perfilId, @PathVariable Long contenidoId) {
        service.eliminar(perfilId, contenidoId);
    }
}
