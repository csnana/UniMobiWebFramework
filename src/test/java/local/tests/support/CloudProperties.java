package local.tests.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class CloudProperties {
    private final Properties properties = new Properties();

    public CloudProperties() {
        try (InputStream input = CloudProperties.class.getResourceAsStream("/cloud.properties")) {
            if (input == null) {
                throw new IllegalStateException(
                        "cloud.properties not found in src/test/resources");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load cloud.properties", e);
        }
    }

    public String required(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required property '" + key + "' in cloud.properties");
        }
        return value.trim();
    }

    public String optional(String key) {
        return properties.getProperty(key, "").trim();
    }
}