package org.testcontainers.containers;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * @author richardnorth
 */
public class MySQLContainer<SELF extends MySQLContainer<SELF>> extends JdbcDatabaseContainer<SELF> {

    public static final String NAME = "mysql";
    public static final String IMAGE = "mysql";
    private static final String MY_CNF_CONFIG_OVERRIDE_PARAM_NAME = "TC_MY_CNF";
    public static final Integer MYSQL_PORT = 3306;
    private String databaseName = "test";
    private String username = "test";
    private String password = "test";

    public MySQLContainer() {
        super(IMAGE + ":latest");
    }

    public MySQLContainer(String dockerImageName) {
        super(dockerImageName);
    }

    @NotNull
    @Override
    protected Set<Integer> getLivenessCheckPorts() {
        return new HashSet<>(getMappedPort(MYSQL_PORT));
    }

    @Override
    protected void configure() {
        optionallyMapResourceParameterAsVolume(MY_CNF_CONFIG_OVERRIDE_PARAM_NAME, "/etc/mysql/conf.d", "mysql-default-conf");

        addExposedPort(3306);
        addEnv("MYSQL_DATABASE", databaseName);
        addEnv("MYSQL_USER", username);
        addEnv("MYSQL_PASSWORD", password);
        addEnv("MYSQL_ROOT_PASSWORD", "test");
        setCommand("mysqld");
        setStartupAttempts(3);
    }

    @Override
    public String getDriverClassName() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:mysql://" + getContainerIpAddress() + ":" + getMappedPort(MYSQL_PORT) + "/" + databaseName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getTestQueryString() {
        return "SELECT 1";
    }

    public SELF withConfigurationOverride(String s) {
        parameters.put(MY_CNF_CONFIG_OVERRIDE_PARAM_NAME, s);
        return self();
    }

    @Override
    public SELF withDatabaseName(final String databaseName) {
        this.databaseName = databaseName;
        return self();
    }

    @Override
    public SELF withUsername(final String username) {
        this.username = username;
        return self();
    }

    @Override
    public SELF withPassword(final String password) {
        this.password = password;
        return self();
    }
}
