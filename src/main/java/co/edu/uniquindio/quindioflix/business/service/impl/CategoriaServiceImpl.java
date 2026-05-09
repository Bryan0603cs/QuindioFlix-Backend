package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.response.CategoriaResponse;
import co.edu.uniquindio.quindioflix.business.service.CategoriaService;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categorias;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        log.debug("Listando categorías");
        return categorias.findAllByOrderByNombreAsc()
                .stream()
                .map(MapperService::categoria)
                .toList();
    }
}
