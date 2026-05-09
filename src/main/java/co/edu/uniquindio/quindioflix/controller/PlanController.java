package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.response.PlanResponse;
import co.edu.uniquindio.quindioflix.business.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService service;

    @GetMapping
    public List<PlanResponse> listar() {
        return service.activos();
    }

    @GetMapping("/{id}")
    public PlanResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }
}
