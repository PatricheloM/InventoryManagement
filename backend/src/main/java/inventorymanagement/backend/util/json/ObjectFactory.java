package inventorymanagement.backend.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;
import java.io.InputStream;

public final class ObjectFactory {

    private ObjectFactory() {}

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectReader reader = mapper.reader();

    public static<T> T produce(InputStream json, Class<T> clazz) throws IOException {
        return reader.readValue(json, clazz);
    }

}
