package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.response.PlanResponse;
import co.edu.uniquindio.quindioflix.business.exception.ResourceNotFoundException;
import co.edu.uniquindio.quindioflix.business.service.PlanService;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planes;

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponse> activos() {
        return planes.findByActivoIgnoreCase("S")
                .stream()
                .map(MapperService::plan)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PlanResponse buscar(Long id) {
        return planes.findById(id)
                .map(MapperService::plan)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", id));
    }
}
