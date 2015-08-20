package kr.co.bitnine.octopus.mockup;

import java.sql.DatabaseMetaData;
import java.sql.Statement;

public class OctopusMockups
{
    public static DatabaseMetaData getMetaData()
    {
        return OctopusMockupDatabaseMetaData.getInstance();
    }

    public static Statement createStatement()
    {
        return new OctopusMockupStatement(OctopusMockupDatabaseMetaData.getInstance());
    }
}
