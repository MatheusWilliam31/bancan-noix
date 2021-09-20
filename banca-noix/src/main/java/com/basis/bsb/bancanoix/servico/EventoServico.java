package com.basis.bsb.bancanoix.servico;
import com.basis.bsb.bancanoix.dominio.Evento;
import com.basis.bsb.bancanoix.repositorio.EventoRepositorio;
import com.basis.bsb.bancanoix.servico.dto.EventoDTO;
import com.basis.bsb.bancanoix.servico.exceptions.ResourceNotFoundException;
import com.basis.bsb.bancanoix.servico.mappers.EventoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventoServico extends EventoDTO implements Serializable {


    private final EventoRepositorio repositorio;
    private final EventoMapper mapper;

    public Page<EventoDTO> findAllPaged(PageRequest pageRequest) {
        Page<Evento> page = repositorio.findAll(pageRequest);
        return page.map(mapper::toDto);
    }

    public EventoDTO findById(Long id) {
        Optional<Evento> obj = repositorio.findById(id);
        Evento entity = obj.orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));
        return mapper.toDto(entity);
    }

    public EventoDTO insert(EventoDTO dto) {
        Evento entity = mapper.toEntity(dto);
        entity = repositorio.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id) {
        try {
            repositorio.deleteById(id);
        }catch ( EmptyResultDataAccessException resultadoEx) {
            throw new ResourceNotFoundException("Evento do ID: "+ id +" não encontrado!");
        }
    }
}
