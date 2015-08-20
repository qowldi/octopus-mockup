package kr.co.bitnine.octopus.mockup;

import org.junit.Test;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MockupTest
{
    @Test
    public void test() throws Exception
    {
        DatabaseMetaData meta = OctopusMockups.getMetaData();

        /*
         * Getting Datasource list
         *      The result set consists of
         *          1. Datasource name
         *          2. Description of the datasource
         */
        System.out.println("* Datasource list");
        ResultSet rs = meta.getCatalogs();
        while (rs.next())
            System.out.println("datasource=" + rs.getString(1) + ", desc=" + rs.getString(2));
        rs.close();

        /*
         * Getting Schema list
         *      The result set consists of
         *          1. Schema name
         *          2. Datasource name
         *          3. Description of the datasource
         *
         * You can also call getSchema(String catalog, String schemaPattern)
         */
        System.out.println("* Schema list");
        rs = meta.getSchemas();
        while (rs.next()) {
            System.out.println("schema=" + rs.getString(1) + ", datasource=" + rs.getString(2) +
                    ", desc=" + rs.getString(3));
        }
        rs.close();

        /* Getting Table list
         *      The result set consists of
         *          1. Datasource name
         *          2. Schema name
         *          3. Table name
         *          4. Table type (TABLE or VIEW)
         *          5. Description of the table
         *
         * You can restrict the result set with catalog name, schema pattern or
         * table name pattern.
         * Please refer to the JDBC specification.
         */
        System.out.println("* Table list");
        rs = meta.getTables("datasource1", null, null, null);
        while (rs.next()) {
            System.out.println("datasource=" + rs.getString(1) + ", schema=" + rs.getString(2) +
                    ", table=" + rs.getString(3) + ", type=" + rs.getString(4) +
                    ", desc=" + rs.getString(5));
        }
        rs.close();

        /* Getting Column list
         *      The result set consists of
         *          1. Datasource name
         *          2. Schema name
         *          3. Table name
         *          4. Column name
         *          5. Column type
         *          6. Column security type (this name and contents will be modified later)
         *          7. Description of the column
         *
         * You can restrict the result set with catalog name, schema pattern,
         * table name pattern or column name pattern.
         * Please refer to the JDBC specification.
         */
        System.out.println("* Column list");
        rs = meta.getColumns("datasource1", null, null, null);
        while (rs.next()) {
            System.out.println("datasource:" + rs.getString(1) + ", schema=" + rs.getString(2) +
                    ", table=" + rs.getString(3) + ", column=" + rs.getString(4) +
                    ", type=" + rs.getString(5) + ", securityType=" + rs.getString(6) +
                    ", desc=" + rs.getString(7));
        }
        rs.close();

        Statement stmt = OctopusMockups.createStatement();

        /*
         * Create user
         */
        System.out.println("* CREATE USER");
        String sql = "CREATE USER jsyang IDENTIFIED BY '0009';";
        System.out.println(sql);
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*
         * Grant
         */
        System.out.println("* GRANT");
        sql = "GRANT SELECT ON \"datasource1.schema1.table1\" TO jsyang;";
        System.out.println(sql);
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*
         * List som privileges
         */
        System.out.println("* List some privileges");
        rs = meta.getTablePrivileges("datasource1", null, null);
        while (rs.next()) {
            System.out.println("datasource=" + rs.getString(1) + ", schema=" + rs.getString(2) +
                    ", table=" + rs.getString(3) + ", grantor=" + rs.getString(4) +
                    ", grantee=" + rs.getString(5) + ", privilege=" + rs.getString(6));
        }
        rs.close();

        /*
         * Revoke
         */
        System.out.println("* REVOKE");
        sql = "REVOKE SELECT ON \"datasource1.schema1.table1\" FROM jsyang";
        System.out.println(sql);
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stmt.close();
    }
}