package org.rj.homectl.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Util {
    private static final ObjectMapper OBJECT_MAPPER;
    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static List<String> listFromString(String input, String delimiter) {
        if (input == null) return List.of();

        return Arrays.asList(StringUtils.split(input, delimiter));
    }

    public static ObjectMapper objectMapper() {
        return OBJECT_MAPPER;
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

    public static String loadStringResource(String path) throws IOException {
        final var resourcePath = path.startsWith("/") ? path : ("/" + path);
        try {
            return Files.readString(Paths.get(Util.class.getResource(resourcePath).toURI()));
        }
        catch (URISyntaxException ex) {
            throw new IOException(String.format("Failed to load resource \"%s\" (%s)", resourcePath, ex.getMessage()), ex);
        }
    }

    public static<T> MapDifference<String, Object> mapDifference(T from, T to) {
        return mapDifference(Util.convertToJsonMap(from), Util.convertToJsonMap(to));
    }

    public static MapDifference<String, Object> mapDifference(Map<String, Object> from, Map<String, Object> to) {
        return Maps.difference(from, to);
    }



}
