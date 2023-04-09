package inventorymanagement.backend.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public final class JsonFactory {

    private JsonFactory() {}

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

    public static JsonNode produce(Object object) throws JsonProcessingException {
        return mapper.readTree(writer.writeValueAsString(object));
    }
}
