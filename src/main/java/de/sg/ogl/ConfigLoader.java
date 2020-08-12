package de.sg.ogl;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Properties;

public class ConfigLoader {
    public static void load(Class<?> configClass, String path) {
        try {
            InputStream input = ConfigLoader.class.getResourceAsStream(path);

            Properties properties = new Properties();

            properties.load(input);

            for (Field field : configClass.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    field.set(null, getValue(properties, field.getName(), field.getType()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getValue(Properties properties, String name, Class<?> type) {
        Objects.requireNonNull(properties);
        Objects.requireNonNull(name);

        String value = properties.getProperty(name);

        if (value == null) {
            throw new IllegalArgumentException("Missing configuration value: " + name);
        }

        if (type == String.class) {
            return value;
        }

        if (type == boolean.class) {
            return Boolean.parseBoolean(value);
        }

        if (type == int.class) {
            return Integer.parseInt(value);
        }

        if (type == float.class) {
            return Float.parseFloat(value);
        }

        throw new IllegalArgumentException("Unknown configuration value type: " + type.getName());
    }
}
