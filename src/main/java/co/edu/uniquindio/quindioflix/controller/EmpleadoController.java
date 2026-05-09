package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.response.EmpleadoResponse;
import co.edu.uniquindio.quindioflix.business.service.EmpleadoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Empleados")
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CONTENIDO') and @authorizationService.isCurrentUserActive()")
    public Page<EmpleadoResponse> listar(Pageable pageable) {
        return service.listar(pageable);
    }
}
