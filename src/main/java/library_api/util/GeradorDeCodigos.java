package library_api.util;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class GeradorDeCodigos {
    public Long gerarCodLivro(){
        return ThreadLocalRandom.current().nextLong(100000L, 1000000L);
    }

    public Long gerarCodCliente(){
        return ThreadLocalRandom.current().nextLong(10000000L, 100000000L);
    }
}
