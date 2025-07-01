package jdbc;
import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Getter
@Setter
public class CustomDataSource implements DataSource {

    private static volatile CustomDataSource instance;

    private final String driver;
    private final String url;
    private final String username;
    private final String password;

    private CustomConnector connector;

    private CustomDataSource(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.connector = new CustomConnector();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load database driver", e);
        }
    }

    public static CustomDataSource getInstance() {
        if (instance == null) {
            synchronized (CustomDataSource.class) {
                if (instance == null) {
                    Properties props = new Properties();
                    try (InputStream input = CustomDataSource.class.getClassLoader().getResourceAsStream("app.properties")) {
                        if (input == null) {
                            throw new RuntimeException("Unable to find app.properties");
                        }
                        props.load(input);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to load app.properties", e);
                    }

                    String driver = props.getProperty("postgres.driver");
                    String url = props.getProperty("postgres.url");
                    String username = props.getProperty("postgres.name");
                    String password = props.getProperty("postgres.password");

                    instance = new CustomDataSource(driver, url, username, password);
                }
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connector.getConnection(url, username, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return connector.getConnection(url, username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public java.io.PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(java.io.PrintWriter out) throws SQLException {
        // no-op
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        // no-op
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public java.util.logging.Logger getParentLogger() {
        return java.util.logging.Logger.getGlobal();
    }
}
