package library_api.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;



public class IsbnSerializer extends StdSerializer<String> {

    public IsbnSerializer() {
        this(null);
    }

    public IsbnSerializer(Class<String> t) {
        super(t);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if(value == null|| value.length() !=13){
            gen.writeString(value);
            return;
        }

        // Aplica a máscara visual padrão do ISBN-13 (XXX-XX-XXX-XXXX-X)
        String isbnFormatado = value.replaceFirst("(\\d{3})(\\d{2})(\\d{3})(\\d{4})(\\d+)", "$1-$2-$3-$4-$5");

        gen.writeString(isbnFormatado);
    }


}
