package config;

import log.Logger;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static Properties load() {
        Properties prop = new Properties();
        try (InputStream input = Config.class
                .getClassLoader()
                .getResourceAsStream("config/config.properties")) {

            if (input == null) {
                throw new RuntimeException("Không tìm thấy file config.properties");
            }
            prop.load(input);
        } catch (Exception e) {
            Logger.log("ERROR","Cannot load config.properties");
        }
        return prop;
    }
}
