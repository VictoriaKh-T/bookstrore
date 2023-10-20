package mate.academy.bookstore.config;

import org.testcontainers.containers.MySQLContainer;

public class CustomMySQLContainer extends MySQLContainer<CustomMySQLContainer> {
    private static final String IMAGE_VERSION = "mysql:8";
    private static CustomMySQLContainer mySQLContainer;
    public CustomMySQLContainer() {
        super(IMAGE_VERSION);
    }

    public static synchronized CustomMySQLContainer getInstance() {
        if (mySQLContainer == null) {
            mySQLContainer = new CustomMySQLContainer();
        }
        return mySQLContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", mySQLContainer.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", mySQLContainer.getUsername());
        System.setProperty("TEST_DB_PASSWORD", mySQLContainer.getPassword());
    }

    @Override
    public void stop() {

    }
}
