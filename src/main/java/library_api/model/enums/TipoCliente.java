package library_api.model.enums;

import lombok.Getter;

@Getter
public enum TipoCliente {
    PF("Pessoa Física"),
    PJ("Pessoa Juridíca");
    
    private final String descricao;

    TipoCliente(String descricao){
        this.descricao = descricao;
    }
}
