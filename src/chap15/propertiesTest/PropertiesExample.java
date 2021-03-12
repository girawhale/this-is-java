package chap15.propertiesTest;

import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

public class PropertiesExample {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        String path = PropertiesExample.class.getResource("test.properties").getPath();
        System.out.println("path : " + path);
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        properties.load(new FileReader(path));

        for (Map.Entry<Object, Object> property : properties.entrySet())
            System.out.println(property.getKey() + " : " + property.getValue());
    }
}
