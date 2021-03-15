package org.rj.homectl.spring.util;

import org.rj.homectl.common.config.Config;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.StreamSupport;

public class SpringUtil {

    @SuppressWarnings("rawtypes")
    public static Properties getPropertiesFromSpringEnvironment(Environment environment) {
        Properties properties = new Properties();
        MutablePropertySources propertySource = ((AbstractEnvironment) environment).getPropertySources();
        StreamSupport.stream(propertySource.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .forEach(propName -> properties.setProperty(propName, environment.getProperty(propName)));

        return properties;
    }

    public static Config getConfigurationFromSpringEnvironment(Environment environment) {
        return Config.load(getPropertiesFromSpringEnvironment(environment));
    }

}
