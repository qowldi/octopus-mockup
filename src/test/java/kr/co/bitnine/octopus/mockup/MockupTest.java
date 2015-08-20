package kr.co.bitnine.octopus.mockup;

import org.junit.Test;

import java.util.Scanner;

public class MockupTest
{
    @Test
    public void test() throws Exception
    {
/*
            Class.forName("kr.co.bitnine.octopus.mockup.Driver");
            String url = "jdbc:octopus-mockup://localhost/db";

             The genuine driver name will be 'ko.co.bitnine.octopus.Driver'
               and the connection url will be 'jdbc:octopus://...'


            Properties info = new Properties();
            info.setProperty("user", "octopus");
            info.setProperty("password", "bitnine");


             Connect to Octopus
               (this mockup driver does nothing for connection)


            Connection conn = DriverManager.getConnection(url, info);
*/
            /* Getting OctopusMockupDatabaseMetaData class
            *OctopusMockupDatabaseMetaData dbmd = conn.getMetaData();
            */
        OctopusMockupDatabaseMetaData dbmd = new OctopusMockupDatabaseMetaData();
            /* 1. Getting Datasource list
                  The result set consists of
                      1. Datasource name
                      2. Description of the datasource
             */
        System.out.println("== Datasource List ==");
        OctopusMockupResultSet datasources = dbmd.getCatalogs();
        while (datasources.next()) {
            System.out.println("Datasource:" + datasources.getString(1) + " Description:" + datasources.getString(2));
        }

            /* 2. Getting Schema list
                  The result set consists of
                      1. Datasource name
                      2. Schema name
                      2. Description of the datasource
             */
        System.out.println("== Schema List ==");
        OctopusMockupResultSet schemas = dbmd.getSchemas();
            /* You can also call the method getSchema(String catalog, String schemaPattern) */
        while (schemas.next()) {
            System.out.println("Datasource:" + schemas.getString(1) + " Schema:" + schemas.getString(2) +
                    " Description:" + schemas.getString(3));
        }

            /* 3. Getting Table list
                  The result set consists of
                      1. Datasource name
                      2. Schema name
                      3. Table name
                      4. Table type (TABLE or VIEW)
                      5. Description of the table
             */
        System.out.println("== Table List ==");
        OctopusMockupResultSet tables = dbmd.getTables(null, null, null, null);
            /* You can restrict the catalog name or schema pattern, table name pattern.
               Please refer to the JDBC specification (OctopusMockupDatabaseMetaData class) */
        while (tables.next()) {
            System.out.println("Datasource:" + tables.getString(1) + " Schema:" + tables.getString(2) +
                            " Table:" + tables.getString(3) + " Type:" + tables.getString(4)
            );// + " Description:" + tables.getString(5));
        }

            /* 4. Getting Column list
                  The result set consists of
                      1. Datasource name
                      2. Schema name
                      3. Table name
                      4. Column name
                      5. Column type
                      6. Column security type (this name and contents will be modified later)
                      7. Description of the column
             */
        System.out.println("== Column List ==");
        OctopusMockupResultSet columns = dbmd.getColumns(null, null, null, null);
        while (columns.next()) {
            System.out.println("Datasource:" + columns.getString(1) + " Schema:" + columns.getString(2) +
                    " Table:" + columns.getString(3) + " Column:" + columns.getString(4) +
                    " Type:" + columns.getString(5) + " Security Type:" + columns.getString(6) + " Description:" + columns.getString(5));
        }


            /* 5. Create user

             */
        System.out.println("== CREATE USER ==");
        System.out.println("DDL : CREATE USER octopus PASSWORD octopus");
        String createUserStmt = "CREATE USER octopus PASSWORD octopus";
        OctopusMockupResultSet addUsers1 = dbmd.createUser(createUserStmt);

        System.out.println("== User List ==");
        OctopusMockupResultSet users = dbmd.getUsers();
        while(users.next()){
            System.out.println("User name :" + users.getString(1));
        }

            /* 6. Grant

             */
        System.out.println("== GRANT  ==");
        String grantStmt1 = "GRANT SELECT ON datasource.schema.table TO user1";
    //  String grantStmt2 = "GRANT SELECT ON schema TO octopus";
    //  String grantStmt3 = "GRANT SELECT ON table TO octopus";
    //  String grantStmt4 = "GRANT ROLE TO octopus";

        dbmd.grantStmt(grantStmt1);
        OctopusMockupResultSet grant = dbmd.getGrant();
        while (grant.next()) {
            System.out.println("1. grantee : " + grant.getString(1) + " 2. grantor : " + grant.getString(2) + " 3. privilege : " + grant.getString(3)
            + " 4. table name : " + grant.getString(6));
        }

            /* 7. REVOKE

             */
        System.out.println("==  REVOKE  ==");
        String revokeStmt1 = "REVOKE SELECT FROM octopus";
     // String revokeStmt2 = "REVOKE SELECT ON datasource FROM octopus";

        OctopusMockupResultSet revoke = dbmd.revokeStmt(revokeStmt1);
        while(revoke.next()){
            System.out.println("Revoke Username : " + revoke.getString(1) + " Revoke obj : " + revoke.getString(2)
                    + " Revoke auth : " + revoke.getString(3));
        }

            /* 8. List of authority

            */
        System.out.println("== List of authority ==");
        OctopusMockupResultSet authList = dbmd.getGrant();

        while (authList.next()) {
            System.out.println("1. grantee : " + authList.getString(1) + " 2. grantor = " + authList.getString(2) +" 3. privilege : " + authList.getString(3) + " 4. datasource : " + authList.getString(4)
                    + " 5. schema : " + authList.getString(5) + " 6. table name : " + authList.getString(6));
        }
    }
}