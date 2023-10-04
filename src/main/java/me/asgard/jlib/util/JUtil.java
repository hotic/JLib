package me.asgard.jlib.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

public class JUtil {

    @Inject
    @Named("yamlObjectMapper")
    private static ObjectMapper objectMapper;

    public static void ConfigInject(File configFile, Object object) {
        try {
            Map<String, Object> map = objectMapper.readValue(configFile, Map.class);
            for (Field field : object.getClass().getFields()) {
                ReflectionUtils.setValue(object, true, field.getName(), map.get(field.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
