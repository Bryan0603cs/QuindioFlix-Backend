package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.response.CategoriaResponse;
import co.edu.uniquindio.quindioflix.business.service.CategoriaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Categorías")
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    @GetMapping
    public List<CategoriaResponse> listar() {
        return service.listar();
    }
}
