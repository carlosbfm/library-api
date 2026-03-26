package library_api.model.enums;

import lombok.Getter;

@Getter
public enum StatusLivro {
    DISPONIVEL("DISPONÍVEL", "Esse livro está disponível!"),
    EMPRESTADO("ENPRESTADO", "Esse livro está emprestado!"),
    PERDIDO("PERDIDO", "Esse livro foi perdido!"),
    DANIFICADO("DANIFICADO","Este livro está danificado!");
    
    private final String status;
    private final String descricao;
    StatusLivro(String status,String descricao){
        this.descricao = descricao;
        this.status = status;
    }
}
