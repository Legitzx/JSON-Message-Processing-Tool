package config;

/**
 * This class is responsible for loading the config
 *
 * @author Luciano Kholos
 */
public class Config {
    private static final ConfigParser config = ConfigParser.load();

    public static < T > T get(String key, Class<T> type) { return config.get(key, type); }
}