package kr.co.bitnine.octopus.mockup.ddl;

import java.sql.SQLException;

public interface OctopusMockupRunner
{
    void createUser(String name, String password) throws SQLException;
    void grantSelect(String objectName, String grantee) throws SQLException;
    void revokeSelect(String objectName, String revokee) throws SQLException;
}
