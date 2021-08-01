package org.rj.homectl.common.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Properties;

public class ConfigTest {

    private Config config;

    @Before
    public void setup() {
        final var properties = new Properties();
        properties.putAll(Map.of(
                "abc", "123",
                "def", "456",
                "ghi", "0${def}0"
        ));

        config = Config.load(properties);
    }

    @Test
    public void testConfigSubstitution() {
        Assert.assertEquals("aaabbbccc", config.performSubstitutions("aaabbbccc"));
        Assert.assertEquals("0012344", config.performSubstitutions("00${abc}44"));
        Assert.assertEquals("123456", config.performSubstitutions("${abc}${def}"));
        Assert.assertEquals("123123123", config.performSubstitutions("${abc}${abc}${abc}"));
        Assert.assertEquals("#04560#", config.performSubstitutions("#${ghi}#"));
        Assert.assertEquals("0000", config.performSubstitutions("00${none}00"));
        Assert.assertEquals("special $ } { chars", config.performSubstitutions("special $ } { chars"));
        Assert.assertEquals("not valid ${ with no closing bracket", config.performSubstitutions("not valid ${ with no closing bracket"));
    }

}
