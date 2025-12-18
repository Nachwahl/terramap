package fr.thesmyler.terramap.util.json;

import com.google.gson.*;
import net.buildtheearth.terraminusminus.projection.GeographicProjection;

import java.lang.reflect.Type;

/**
 * JSON adapter for GeographicProjection
 * 
 * Note: terraminusminus projections use Jackson for serialization.
 * This adapter bridges between Gson (used by Minecraft/Terramap) and
 * Jackson (used by terraminusminus).
 * 
 * TODO: Implement proper projection serialization/deserialization
 * For now, this is a stub that needs to be completed when projection
 * configuration is implemented.
 */
public class ProjectionAdapter implements JsonSerializer<GeographicProjection>, JsonDeserializer<GeographicProjection> {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    public GeographicProjection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // TODO: Implement projection deserialization
        // This needs to:
        // 1. Parse the JSON to get projection type and parameters
        // 2. Use terraminusminus's projection registry to create the right projection
        // 3. Apply any transforms specified in the JSON
        throw new UnsupportedOperationException("Projection deserialization not yet implemented - TODO");
    }

    @Override
    public JsonElement serialize(GeographicProjection src, Type typeOfSrc, JsonSerializationContext context) {
        // TODO: Implement projection serialization
        // This needs to:
        // 1. Get the projection type and parameters
        // 2. Serialize to JSON format compatible with terraminusminus
        // 3. Include any transforms applied to the projection
        throw new UnsupportedOperationException("Projection serialization not yet implemented - TODO");
    }

}
