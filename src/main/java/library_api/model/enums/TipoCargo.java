package library_api.model.enums;

import lombok.Getter;

@Getter
public enum TipoCargo {

    ADMINISTRADOR("Administrador", "ADMIN", "Acesso irrestrito ao sistema técnico e gestão de credenciais."),
    COORDENADOR("Coordenador", "COORDENADOR", "Supervisão da equipe, emissão de relatórios gerenciais e auditoria de acervo."),
    BIBLIOTECARIO("Bibliotecário", "BIBLIOTECARIO", "Gestão especializada do acervo, catalogação, curadoria de livros e autores."),
    ASSISTENTE_ADMINISTRATIVO("Assistente Administrativo", "ASSISTENTE", "Apoio operacional ao sistema, organização de fluxos e suporte à coordenação."),
    ATENDENTE("Atendente", "ATENDENTE", "Operação direta no balcão, realizando o cadastro de leitores, empréstimos e devoluções."),
    ESTAGIARIO("Estagiário", "ESTAGIARIO", "Auxílio no atendimento e consultas básicas de disponibilidade de livros no sistema."),
    DESENVOLVEDOR("Desenvolvedor", "ADMIN", "Acesso irrestrito ao sistema técnico e gestão de credenciais.");


    private final String titulo; 
    private final String role;
    private final String descricao;

    TipoCargo(String titulo, String role, String descricao) {
        this.titulo = titulo;
        this.role = role;
        this.descricao = descricao;
    }
}