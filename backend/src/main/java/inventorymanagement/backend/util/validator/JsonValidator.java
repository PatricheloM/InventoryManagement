package inventorymanagement.backend.util.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import inventorymanagement.backend.util.exception.SchemaNotFoundException;

public class JsonValidator {

    public static final JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

    public static<T> boolean validate(JsonNode json, Class<T> clazz) throws SchemaNotFoundException
    {
        try {
            JsonSchema schema = jsonSchemaFactory.getSchema(JsonValidator.class.getResourceAsStream(clazz.getSimpleName() + ".json"));
            return schema.validate(json).isEmpty();
        } catch (Exception e) {
            throw new SchemaNotFoundException(e);
        }
    }
}
