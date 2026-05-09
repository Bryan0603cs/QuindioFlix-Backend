package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.response.EmpleadoResponse;
import co.edu.uniquindio.quindioflix.business.service.EmpleadoService;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleados;

    @Override
    @Transactional(readOnly = true)
    public Page<EmpleadoResponse> listar(Pageable pageable) {
        return empleados.findAll(pageable)
                .map(MapperService::empleado);
    }
}
