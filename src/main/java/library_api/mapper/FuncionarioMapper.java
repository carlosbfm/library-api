package library_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import library_api.dto.request.FuncionarioRequestDTO;
import library_api.dto.response.FuncionarioResponseDTO;
import library_api.model.entity.funcionario.FuncionarioEntity;

@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    @Mapping(source = "cargo", target = "cargo.id")
    @Mapping(target = "matricula", ignore = true)
    @Mapping(target = "dataAdmissao", ignore = true)
    @Mapping(target = "usuario", ignore = true) 
    FuncionarioEntity toEntity(FuncionarioRequestDTO dto); // Pega os dados do DTO e transforma PARA Entidade.
    
    @Mapping(source = "cargo.salarioBase", target = "salarioBase")
    @Mapping(source = "cargo.nomeCargo", target = "cargo")
    FuncionarioResponseDTO toDto(FuncionarioEntity funcionario); // Pega os dados da Entidade e transforma PARA DTO.

}