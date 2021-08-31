package org.rj.homectl.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Consumer;

public class Util {
    private static final ObjectMapper OBJECT_MAPPER;
    private static final ObjectMapper OBJECT_MAPPER_YAML;
    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        OBJECT_MAPPER_YAML = new ObjectMapper(new YAMLFactory());
        OBJECT_MAPPER_YAML.registerModule(new JavaTimeModule());
        OBJECT_MAPPER_YAML.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static List<String> listFromString(String input, String delimiter) {
        if (input == null) return List.of();

        return Arrays.asList(StringUtils.split(input, delimiter));
    }

    public static ObjectMapper objectMapper() {
        return OBJECT_MAPPER;
    }
    public static ObjectMapper objectMapperYaml() {
        return OBJECT_MAPPER_YAML;
    }

    public static String safeSerialize(Object obj) {
        try {
            return objectMapper().writeValueAsString(obj);
        }
        catch (Throwable t) {
            return String.format("[Serialization failed: %s]", t);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> convertToJsonMap(T object) {
        return (Map<String, Object>)objectMapper().convertValue(object, Map.class);
    }

    public static void threadSleepOrElse(long sleepMs, Consumer<InterruptedException> onInterruption) {
        try {
            Thread.sleep(sleepMs);
        }
        catch (InterruptedException ex) {
            onInterruption.accept(ex);
        }
    }

    public static Result<String, String> loadStringResource(String resourcePath) {
        try (final var resource = Util.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (resource == null ) throw new RuntimeException(String.format(
                    "Could not open resource stream at '%s'", resourcePath));

            return Result.Ok(IOUtils.toString(resource, Charset.defaultCharset()));
        }
        catch (IOException ex) {
            return Result.Err(String.format("Failed to load resource \"%s\" (%s)", resourcePath, ex.getMessage()));
        }
    }

    public static <T> Result<String, String> serializeJson(final T object) {
        return trySerialize(objectMapper(), object);
    }

    public static <T> Result<String, String> serializeYaml(final T object) {
        return trySerialize(objectMapperYaml(), object);
    }

    private static <T> Result<String, String> trySerialize(final ObjectMapper mapper, final T object) {
        try {
            return Result.Ok(mapper.writeValueAsString(object));
        }
        catch (IOException ex) {
            return Result.Err(ex.getMessage());
        }
    }

    public static <T> Result<T, String> deserializeJson(final String json, final Class<T> cls) {
        return tryDeserialize(objectMapper(), json, cls);
    }

    public static <T> Result<T, String> deserializeYaml(final String yaml, final Class<T> cls) {
        return tryDeserialize(objectMapperYaml(), yaml, cls);
    }

    private static <T> Result<T, String> tryDeserialize(final ObjectMapper mapper, final String data, final Class<T> cls) {
        try {
            return Result.Ok(mapper.readValue(data, cls));
        }
        catch (IOException ex) {
            return Result.Err(ex.getMessage());
        }
    }
}