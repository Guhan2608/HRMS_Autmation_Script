package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

    private Properties properties;

    public PropertyReader(String fileName) {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream( fileName);
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not load property file: " + fileName, e);
        }
    }

    public  String get(String key) {
        return properties.getProperty(key);
    }
}
