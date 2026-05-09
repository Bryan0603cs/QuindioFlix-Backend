package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.response.CategoriaResponse;
import co.edu.uniquindio.quindioflix.business.service.CategoriaService;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categorias;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categorias.findAllByOrderByNombreAsc()
                .stream()
                .map(MapperService::categoria)
                .toList();
    }
}
