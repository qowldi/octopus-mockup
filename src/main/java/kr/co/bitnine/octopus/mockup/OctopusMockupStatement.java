package kr.co.bitnine.octopus.mockup;

import kr.co.bitnine.octopus.mockup.ddl.OctopusMockupCommand;
import kr.co.bitnine.octopus.mockup.ddl.OctopusMockupDdl;
import kr.co.bitnine.octopus.mockup.ddl.OctopusMockupRunner;

import java.sql.SQLException;

class OctopusMockupStatement extends AbstractStatement
{
    private final OctopusMockupDatabaseMetaData meta;
    private boolean isClosed;

    OctopusMockupStatement(OctopusMockupDatabaseMetaData meta)
    {
        this.meta = meta;
        isClosed = false;
    }

    @Override
    public void close() throws SQLException
    {
        isClosed = true;
    }

    @Override
    public boolean execute(String s) throws SQLException
    {
        if (isClosed)
            throw new SQLException("statement is already closed");

        OctopusMockupCommand cmd = OctopusMockupDdl.parse(s);
        OctopusMockupDdl.run(cmd, new OctopusMockupRunner() {
            @Override
            public void createUser(String name, String password) throws SQLException
            {
                if (!meta.createUser(name, password))
                    throw new SQLException("failed to create user '" + name + "'");
            }

            @Override
            public void grantSelect(String objectName, String grantee) throws SQLException
            {
                if (!meta.grantSelect(objectName, grantee))
                    throw new SQLException("failed to grant SELECT on '" + objectName + "' to user '" + grantee + "'");
            }

            @Override
            public void revokeSelect(String objectName, String revokee) throws SQLException
            {
                if (!meta.revokeSelect(objectName, revokee))
                    throw new SQLException("failed to revoke SELECT on '" + objectName + "' from user '" + revokee + "'");
            }
        });

        return false;
    }
}
