package inventorymanagement.backend.util.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import inventorymanagement.backend.util.exception.SchemaNotFoundException;

import java.util.HashMap;
import java.util.LinkedHashMap;

public final class JsonValidator {

    private JsonValidator() {}

    private static final JsonValidator INSTANCE = new JsonValidator();

    public static synchronized JsonValidator getInstance()
    {
        return INSTANCE;
    }

    private final HashMap<String, JsonSchema> cache = new LinkedHashMap<>();

    private final JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

    public <T> boolean validate(JsonNode json, Class<T> clazz) throws SchemaNotFoundException
    {
        try {
            if (cache.containsKey(clazz.getSimpleName())) return cache.get(clazz.getSimpleName()).validate(json).isEmpty();
            JsonSchema schema = jsonSchemaFactory.getSchema(JsonValidator.class.getResourceAsStream(clazz.getSimpleName() + ".json"));
            cache.put(clazz.getSimpleName(), schema);
            return schema.validate(json).isEmpty();
        } catch (Exception e) {
            throw new SchemaNotFoundException(e);
        }
    }
}
