package library_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import library_api.dto.request.ClienteRequestDTO;
import library_api.dto.response.ClienteResponseDTO;
import library_api.model.entity.cliente.ClienteEntity;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "codCliente", ignore = true)
    @Mapping(target = "data", ignore = true)
    ClienteEntity toEntity(ClienteRequestDTO dto);

    @Mapping(source = "tipoCliente.descricao", target = "tipoCliente")
    ClienteResponseDTO toDto(ClienteEntity cliente);
}
