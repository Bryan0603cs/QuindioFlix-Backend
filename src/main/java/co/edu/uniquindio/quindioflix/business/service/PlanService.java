package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.response.PlanResponse;

import java.util.List;

public interface PlanService {

    List<PlanResponse> activos();

    PlanResponse buscar(Long id);
}
