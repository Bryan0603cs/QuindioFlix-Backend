package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.response.EmpleadoResponse;
import co.edu.uniquindio.quindioflix.business.service.EmpleadoService;
import co.edu.uniquindio.quindioflix.persistence.entity.EmpleadoEntity;
import co.edu.uniquindio.quindioflix.persistence.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<EmpleadoResponse> listar(Pageable pageable) {
        log.info("Listando empleados paginados page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        return empleadoRepository.findAll(pageable)
                .map(this::mapearEmpleado);
    }

    private EmpleadoResponse mapearEmpleado(EmpleadoEntity empleado) {
        return new EmpleadoResponse(
                empleado.getId(),
                empleado.getNombre(),
                empleado.getCargo(),
                empleado.getDepartamento() != null ? empleado.getDepartamento().getId() : null,
                empleado.getDepartamento() != null ? empleado.getDepartamento().getNombre() : null
        );
    }
}