package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.response.GeneroResponse;
import co.edu.uniquindio.quindioflix.business.service.GeneroService;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.GeneroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneroServiceImpl implements GeneroService {

    private final GeneroRepository generos;

    @Override
    @Transactional(readOnly = true)
    public List<GeneroResponse> listar() {
        log.debug("Listando géneros");
        return generos.findAllByOrderByNombreAsc()
                .stream()
                .map(MapperService::genero)
                .toList();
    }
}
