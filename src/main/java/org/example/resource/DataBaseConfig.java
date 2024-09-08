package org.example.resource;

public enum DataBaseConfig {
    dbHost("dbHost"),
    dbPort("dbPort"),
    dbUser("dbUser"),
    dbPass("dbPass"),
    dbName("dbName");

    private String configKey;

    private DataBaseConfig(String configKey) {
        this.configKey = configKey;
    }

    public String getValue() {
        // return System.getenv(configKey);
        return ConfigReader.loadConfig().getProperty(configKey);
    }
}

