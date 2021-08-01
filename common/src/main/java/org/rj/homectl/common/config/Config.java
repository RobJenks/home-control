package org.rj.homectl.common.config;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Config {
    static final Pattern substPattern = Pattern.compile("\\$\\{([^}]+)\\}");

    private final Properties properties;

    // Parent config can optionally be provided to use in resolving embedded ${variables} in the loaded config
    public static Config load(String path, Config parentConfig) {
        try (final var resource = Config.class.getClassLoader().getResourceAsStream(path)) {
            if (resource == null ) throw new RuntimeException(String.format(
                    "Could not open resource stream for config at '%s'", path));

            Properties properties = new Properties();
            properties.load(resource);

            final var config = new Config(properties);

            if (parentConfig != null) {
                config.resolvePlaceholders(parentConfig);
            }

            return config;
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

    private void resolvePlaceholders(Config parentConfig) {
        final var resolved = properties.entrySet().stream()
                .map(entry -> ImmutablePair.of(
                        parentConfig.performSubstitutionsIfApplicable(entry.getKey()),
                        parentConfig.performSubstitutionsIfApplicable(entry.getValue())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        properties.clear();
        properties.putAll(resolved);
    }

    private Object performSubstitutionsIfApplicable(Object target) {
        if (!(target instanceof String)) return target;
        return performSubstitutions((String)target);
    }

    public String performSubstitutions(String target) {
        var resolved = target;
        var matcher = substPattern.matcher(target);

        while (matcher.find()) {
            resolved = resolved.replace(matcher.group(0), tryGet(matcher.group(1)).orElse(""));
            matcher = substPattern.matcher(resolved);
        }

        return resolved;
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

    public Optional<String> tryGet(ConfigEntry entry) {
        return Optional.ofNullable(entry)
                .map(ConfigEntry::getKey)
                .flatMap(this::tryGet);
    }
    public Optional<String> tryGet(String entry) {
        return Optional.ofNullable(entry)
                .filter(properties::containsKey)
                .map(this::get);
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

    public Optional<Boolean> tryGetBoolean(ConfigEntry entry) {
        return Optional.ofNullable(entry)
                .map(ConfigEntry::getKey)
                .flatMap(this::tryGetBoolean);
    }
    public Optional<Boolean> tryGetBoolean(String entry) {
        return Optional.ofNullable(entry)
                .filter(properties::containsKey)
                .map(this::getBoolean);
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
