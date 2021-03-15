package org.rj.homectl.common.config;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Config {
    private final Properties properties;

    public static Config load(String path) {
        try (final var resource = Config.class.getClassLoader().getResourceAsStream(path)) {
            if (resource == null ) throw new RuntimeException(String.format(
                    "Could not open resource stream for config at '%s'", path));

            final Properties properties = new Properties();
            properties.load(resource);

            return new Config(properties);
        }
        catch (IOException ex) {
            throw new RuntimeException("Could not load configuration: " + ex.getMessage(), ex);
        }
    }

    public static Config load(Properties properties) {
        return new Config(properties);
    }

    private Config(final Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public Map<String, Object> toMap() {
        return properties.entrySet().stream()
                .collect(Collectors.toMap(x -> (String)x.getKey(), Map.Entry::getValue));
    }

    public String get(ConfigEntry entry) {
        return get(entry.getKey());
    }
    public String get(String entry) {
        return getAs(entry, Function.identity());
    }

    public Integer getInteger(ConfigEntry entry) {
        return getInteger(entry.getKey());
    }
    public Integer getInteger(String entry) {
        return getAs(entry, Integer::parseInt);
    }

    public Long getLong(ConfigEntry entry) {
        return getLong(entry.getKey());
    }
    public Long getLong(String entry) {
        return getAs(entry, Long::parseLong);
    }

    public Boolean getBoolean(ConfigEntry entry) {
        return getBoolean(entry.getKey());
    }
    public Boolean getBoolean(String entry) {
        return getAs(entry, Boolean::parseBoolean);
    }

    public <T> T getAs(ConfigEntry entry, Function<String, T> transform) {
        return getAs(entry.getKey(), transform);
    }

    public <T> T getAs(String entry, Function<String, T> transform) {
        try {
            final var value = properties.getProperty(entry);
            if (value == null) throw new RuntimeException("Could not retrieve missing config property: " + entry);

            return transform.apply(value);
        }
        catch (Exception ex) {
            throw new RuntimeException(String.format("Could not read property '%s': (%s)", entry, ex.getMessage()), ex);
        }
    }

}
