package org.rj.homectl.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
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

    public static void threadSleepOrElse(long sleepMs, Consumer<InterruptedException> onInterruption) {
        try {
            Thread.sleep(sleepMs);
        }
        catch (InterruptedException ex) {
            onInterruption.accept(ex);
        }
    }
}
