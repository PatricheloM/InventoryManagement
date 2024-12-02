package inventorymanagement.backend.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonFactory {

    private JsonFactory() {}

    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode produce(Object object) throws JsonProcessingException {
        return mapper.valueToTree(object);
    }
}
