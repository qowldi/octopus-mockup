package kr.co.bitnine.octopus.mockup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class OctopusMockupDatabaseMetaData {

    private static final char SEARCH_STRING_ESCAPE = '\\';
    static List<Datasource> meta_info;
    static List<User> user_info;

    public OctopusMockupDatabaseMetaData(){
        meta_info = Arrays.asList(
                new Datasource("Datasourc 1", "Oracle 1", Arrays.asList(
                        new Schema("Schema 1", "schema 1", Arrays.asList(
                                new Table("Table 1", "Table", "table 1", Arrays.asList(
                                        new Column("Column 1", "VARCHAR(200)", "Confidential", "column 1"),
                                        new Column("Column 2", "VARCHAR(200)", "Public", "column 2"),
                                        new Column("Column 3", "VARCHAR(200)", "Private", "column 3")
                                ))
                        ))
                )),
        new Datasource("Datasource 2", "Oracle 2", Arrays.asList(
                new Schema("Schema 2", "schema 2", Arrays.asList(
                        new Table("Table 2", "Table", "table 2", Arrays.asList(
                                new Column("Column 1", "VARCHAR(200)", "Confidential", "column 1"),
                                new Column("Column 2", "VARCHAR(200)", "Public", "column 2"),
                                new Column("Column 3", "VARCHAR(200)", "Private", "column 3")
                        ))
                ))
            ))
        );
/*
        user_info = Arrays.asList(
                new User("User 1", "user1"),
                new User("User 2", "user2")
        );
*/
    }

    class Datasource {
        String datasource_name;
        String comment;
        public List<Schema> schemas;

        public Datasource (String datasource_name, String comment, List<Schema> schemas){
            this.datasource_name = datasource_name;
            this.comment = comment;
            this.schemas = schemas;
        }
    }

    class Schema {
        String schema_name;
        String comment;
        public List<Table> tables;

        public Schema(String schema_name, String comment, List<Table> tables){
            this.schema_name = schema_name;
            this.comment = comment;
            this.tables = tables;
        }
    }

    class Table {
        String table_name;
        String type;
        String comment;
        public List<Column> columns;

        public Table ( String table_name, String type, String comment, List<Column> columns) {
            this.table_name = table_name;
            this.type = type;
            this.comment = comment;
            this.columns = columns;
        }
    }

    class Column {
        String column_name;
        String type;
        String secu_type;
        public String comment;

        public Column (String column_name, String type, String secu_type, String comment) {
            this.column_name = column_name;
            this.type = type;
            this.comment = comment;
            this.secu_type = secu_type;
        }
    }

    class User {
        String name;
        String password;
        public List<Role> role;

        public User ( String name, String password) {
            this.name = name;
            this.password = password;
        }
    }

    class Grant {

    }

    class Role {
        String name;
        String granter;
        String grantee;

        public Role ( String name) {
            this.name = name;
        }
    }

    public OctopusMockupResultSet createUser(String ddl) {
        OctopusMockupResultSet resultSet = new OctopusMockupResultSet();
        String ddlStmt = ddl;
        List<String> addUser = parsingStmd(ddlStmt);

        try {
            User user = new User(addUser.get(2), addUser.get(4));
            user_info = Arrays.asList(user);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        return resultSet;
    }

    public OctopusMockupResultSet grant(String grantStmt) {
        OctopusMockupResultSet resultSet = new OctopusMockupResultSet();
        String ddlStmt = grantStmt;
        List<String> grant = parsingStmd(ddlStmt);

        try {


        }catch (NullPointerException e){
            e.printStackTrace();
        }

        return resultSet;
    }


    List parsingStmd(String ddlstmt) {
        List<String> stmt = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(ddlstmt);

        while(st.hasMoreTokens()) {
            String temp = st.nextToken();
            stmt.add(temp);
        }

        /*CASE 1. CREATE USER*/
        if(stmt.get(0).toLowerCase().equals("create") && stmt.get(1).toLowerCase().equals("user") && stmt.get(3).toLowerCase().equals("password"))
            return stmt;
        /*CASE 2. GRANT */
        else if(stmt.get(0).toLowerCase().equals("grant"))
            return stmt;
        /*CASE 3. REVOKE */
        else if(stmt.get(0).toLowerCase().equals("revoke"))
            return stmt;
        else
            return null;
    }

    public OctopusMockupResultSet getUsers() {
        OctopusMockupResultSet resultSet = new OctopusMockupResultSet();

        for(User user : user_info){
            List<String> tuple = new ArrayList<String>();
            tuple.add(user.name);
            resultSet.addTuple(tuple);
        }

        return resultSet;
    }

    public OctopusMockupResultSet getCatalogs() {
        OctopusMockupResultSet resultSet = new OctopusMockupResultSet();

        for(Datasource dataSource : meta_info){
            List<String> tuple = new ArrayList<String>();
            tuple.add(dataSource.datasource_name);
            tuple.add(dataSource.comment);
            resultSet.addTuple(tuple);
        }

        return resultSet;
    }

    public OctopusMockupResultSet getSchemas() {
        OctopusMockupResultSet resultSet = new OctopusMockupResultSet();

        for(Datasource datasource : meta_info){
            for(Schema schema : datasource.schemas) {
                List<String> tuple = new ArrayList<String>();
                tuple.add(datasource.datasource_name);
                tuple.add(schema.schema_name);
                tuple.add(schema.comment);
                resultSet.addTuple(tuple);
            }
        }

        return resultSet;
    }

    public OctopusMockupResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String type[]) {
        OctopusMockupResultSet resultSet = new OctopusMockupResultSet();
        String regTableNamePattern = convertPattern(tableNamePattern);
        String regSchemaNamePattern = convertPattern(schemaPattern);

        for(Datasource datasource : meta_info) {
            if(catalog != null && !catalog.equals(datasource.datasource_name))
                continue;

            String catalog_name = datasource.datasource_name;

            for (Schema schema : datasource.schemas) {
                if (regSchemaNamePattern != null && !schema.schema_name.matches(regSchemaNamePattern))
                    continue;

                String schema_name = schema.schema_name;

                for(Table table : schema.tables) {
                    if(regTableNamePattern != null && !table.table_name.matches(regTableNamePattern))
                        continue;

                    String table_name = table.table_name;

                    resultSet.addTuple(Arrays.asList(catalog_name, schema_name, table_name, table.comment));
                }
            }
        }

        return resultSet;
    }

    public OctopusMockupResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) {
        OctopusMockupResultSet resultSet = new OctopusMockupResultSet();
        String regTableNamePattern = convertPattern(tableNamePattern);
        String regSchemaNamePattern = convertPattern(schemaPattern);
        String regColumnNamePattern = convertPattern(columnNamePattern);

        for(Datasource datasource: meta_info) {
            if (catalog != null && !catalog.equals(datasource.datasource_name))
                continue;

            String catalog_name = datasource.datasource_name;

            for (Schema schema : datasource.schemas) {
                if (regSchemaNamePattern != null && !schema.schema_name.matches(regSchemaNamePattern))
                    continue;

                String schema_name = schema.schema_name;

                for(Table table : schema.tables) {
                    if (regTableNamePattern != null && !table.table_name.matches(regTableNamePattern))
                        continue;

                        String table_name = table.table_name;

                        for (Column column : table.columns) {
                            if (regColumnNamePattern != null && !column.column_name.matches(regColumnNamePattern))
                                continue;

                                resultSet.addTuple(Arrays.asList(catalog_name, schema_name, table_name, column.column_name, column.type, column.secu_type, column.comment));
                            }
                }
            }
        }

        return resultSet;
    }

    private String convertPattern(final String pattern) {
        if(pattern == null)
            return ".*";
        else {
            StringBuilder result = new StringBuilder(pattern.length());

            boolean escaped = false;
            for(int i = 0, len = pattern.length(); i < len; i++){
                char c = pattern.charAt(i);
                if(escaped) {
                    if ( c != SEARCH_STRING_ESCAPE) {
                        escaped = false;
                    }
                    result.append(c);
                } else {
                    if (c == SEARCH_STRING_ESCAPE) {
                        escaped = true;
                        continue;
                    } else if ( c == '%') {
                        result.append(".*");
                    } else if ( c == '_') {
                        result.append('.');
                    } else {
                        result.append(c);
                    }
                }
            }

            return result.toString();
        }
    }
}
