package org.rj.homectl.kafka.serialization;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.rj.homectl.common.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class AbstractSerializer<T> implements Serializer<T> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSerializer.class);

    @Override
    public byte[] serialize(String topic, Object obj) {
        try {
            return Util.objectMapper().writeValueAsBytes(obj);
        }
        catch (IOException ex) {
            final var error = String.format("Serialization error processing custom object: %s", ex.getMessage());

            LOG.error(error);
            throw new RuntimeException(error, ex);
        }
    }

    @Override
    public void configure(Map configs, boolean isKey) { }

    @Override
    public void close() { }
}
