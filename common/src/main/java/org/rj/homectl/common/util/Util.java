package org.rj.homectl.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class Util {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
}
