package library_api.util;

public class DocumentoUtil {
    private DocumentoUtil(){}

    public static String limpaFormatacao(String documento){
        if(documento == null || documento.isBlank()){
            return documento;
        }
        return documento.replaceAll("\\D", "");
    }
}
