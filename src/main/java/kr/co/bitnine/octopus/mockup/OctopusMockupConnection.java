package kr.co.bitnine.octopus.mockup;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

class OctopusMockupConnection extends AbstractConnection
{
    private boolean isClosed;

    @Override
    public Statement createStatement() throws SQLException
    {
        if (isClosed)
            throw new SQLException("connection is already closed");

        return new OctopusMockupStatement(OctopusMockupDatabaseMetaData.getInstance());
    }

    @Override
    public void close() throws SQLException
    {
        isClosed = true;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException
    {
        return OctopusMockupDatabaseMetaData.getInstance();
    }
}
