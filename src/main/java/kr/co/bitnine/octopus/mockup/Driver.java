package kr.co.bitnine.octopus.mockup;

import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;

public class Driver implements java.sql.Driver
{
    private static Driver registeredDriver;

    static
    {
        try {
            register();
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void register() throws SQLException
    {
        if (registeredDriver != null)
            throw new IllegalStateException("Driver is already registered. It can only be registered once.");

        Driver registeredDriver = new Driver();
        DriverManager.registerDriver(registeredDriver);
        Driver.registeredDriver = registeredDriver;
    }

    @Override
    public java.sql.Connection connect(String url, Properties info) throws SQLException
    {
        return new OctopusMockupConnection();
    }

    @Override
    public boolean acceptsURL(String url)
    {
        return url.startsWith("jdbc:octopus-mockup:");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties) throws SQLException
    {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion()
    {
        return 0;
    }

    @Override
    public int getMinorVersion()
    {
        return 1;
    }

    @Override
    public boolean jdbcCompliant()
    {
        return false;
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        throw new SQLFeatureNotSupportedException();
    }
}

