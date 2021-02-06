package org.rj.homectl.kafka.serialization;

import org.apache.kafka.common.serialization.Deserializer;
import org.rj.homectl.common.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class AbstractDeserializer<T> implements Deserializer<T> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDeserializer.class);
    private final Class<T> deserialisationClass;

    public AbstractDeserializer(final Class<T> deserialisationClass) {
        this.deserialisationClass = deserialisationClass;
    }

    @Override
    public T deserialize(String topic, byte[] bytes) {
        try {
            return Util.objectMapper().readValue(bytes, deserialisationClass);
        }
        catch (IOException ex) {
            final var error = String.format("Deserialization error processing \"%s\" event: %s (%s)",
                    deserialisationClass.getSimpleName(), ex.getMessage(), new String(bytes));

            LOG.error(error, ex);
            throw new RuntimeException(error, ex);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) { }

    @Override
    public void close() { }

}
