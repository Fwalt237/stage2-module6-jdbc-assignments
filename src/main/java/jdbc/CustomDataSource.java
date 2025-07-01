package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;

    public CustomDataSource(String driver, String url, String password, String name) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver"); // Load PostgreSQL driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        }
        this.driver = driver;
        this.url = url;
        this.name = name;
        this.password = password;
    }


    public static CustomDataSource getInstance() {
        if (instance == null) {
            synchronized (CustomDataSource.class) {
                if (instance == null) {
                    try {
                        instance = new CustomDataSource(
                                "org.postgresql.Driver",
                                "jdbc:postgresql://localhost:5432/myfirstdb?user=postgres&password=postgres",
                                "postgres",
                                "postgres"
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException("Failed to initialize database", e);
                    }
                }
            }
        }
        return instance;
    }
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/myfirstdb?user=postgres&password=postgres", "postgres", "postgres");
    }
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/myfirstdb?user=postgres&password=postgres", username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
