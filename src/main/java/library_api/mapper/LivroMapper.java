package library_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import library_api.dto.request.LivroRequestDTO;
import library_api.dto.response.LivroResponseDTO;
import library_api.model.entity.livro.LivroEntity;
import library_api.model.enums.StatusLivro;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    @Mapping(target = "codLivro", ignore = true)
    @Mapping(target = "status", ignore = true)
    LivroEntity toEntity(LivroRequestDTO dto);

    @Mapping(target = "isbn", expression = "java(library_api.util.DocumentoUtil.formataIsbn(livro.getIsbn()))")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    LivroResponseDTO toDto(LivroEntity livro);


    @Mapping(target = "codLivro", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isbn", ignore = true) 
    void atualizarDeDto(LivroRequestDTO dto, @MappingTarget LivroEntity entidade);

    @Named("statusToString")
    default String statusToString(StatusLivro status) {
        return status != null ? status.name() : null;
    }
}