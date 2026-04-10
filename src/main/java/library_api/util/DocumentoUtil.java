package library_api.util;

public class DocumentoUtil {
    
    // Construtor privado para impedir instanciação (Excelente prática mantida)
    private DocumentoUtil(){}

    // Método de Entrada: Limpa qualquer pontuação antes de salvar no Banco de Dados
    public static String limpaFormatacao(String documento){
        if(documento == null || documento.isBlank()){
            return documento;
        }
        return documento.replaceAll("\\D", "");
    }

    // Método de Saída: Desenha a máscara do ISBN para o Postman
    public static String formataIsbn(String isbn) {
        if (isbn == null || isbn.length() != 13) {
            return isbn;
        }
        return isbn.replaceFirst("(\\d{3})(\\d{2})(\\d{3})(\\d{4})(\\d+)", "$1-$2-$3-$4-$5");
    }
}