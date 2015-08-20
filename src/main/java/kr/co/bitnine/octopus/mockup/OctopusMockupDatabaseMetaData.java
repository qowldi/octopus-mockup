package kr.co.bitnine.octopus.mockup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OctopusMockupDatabaseMetaData extends AbstractDatabaseMetaData
{
    private List<Datasource> metaInfo;
    private List<User> userInfo;
    private List<Grant> grantInfo;

    private OctopusMockupDatabaseMetaData()
    {
        userInfo = new ArrayList<>();
        userInfo.add(new User("octopus", "bitnine"));

        grantInfo = new ArrayList<>();
    }

    public void setMetaInfo(List<Datasource> metaInfo)
    {
        this.metaInfo = metaInfo;
    }

    private static OctopusMockupDatabaseMetaData instance = null;

    static OctopusMockupDatabaseMetaData getInstance()
    {
        if (instance == null) {
            synchronized (OctopusMockupDatabaseMetaData.class) {
                if (instance == null)
                    instance = new OctopusMockupDatabaseMetaData();
            }
        }

        return instance;
    }

    private class User
    {
        String name;
        String password;

        User(String name, String password)
        {
            this.name = name;
            this.password = password;
        }
    }

    private class Grant
    {
        String datasourceName;
        String schemaName;
        String tableName;
        final String grantor = "octopus";
        String grantee;
        String privilege;

        public Grant(String datasourceName, String schemaName, String tableName, String grantee, String privilege)
        {
            this.datasourceName = datasourceName;
            this.schemaName = schemaName;
            this.tableName = tableName;
            this.grantee = grantee;
            this.privilege = privilege;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (!(obj instanceof Grant))
                return false;

            Grant g = (Grant) obj;

            if (!datasourceName.equals(g.datasourceName))
                return false;
            if (!schemaName.equals(g.schemaName))
                return false;
            if (!tableName.equals(g.tableName))
                return false;
            if (!grantee.equals(g.grantee))
                return false;
            if (!privilege.equals(g.privilege))
                return false;

            return true;
        }
    }

    private User findUserByName(String name)
    {
        for (User user : userInfo) {
            if (user.name.equals(name))
                return user;
        }

        return null;
    }

    boolean createUser(String name, String password)
    {
        User e = findUserByName(name);
        if (e != null)
            return false;

        User user = new User(name, password);
        userInfo.add(user);
        return true;
    }

    private Grant findGrant(Grant g)
    {
        for (Grant tmp : grantInfo) {
            if (tmp.equals(g))
                return tmp;
        }

        return null;
    }

    boolean grantSelect(String objectName, String grantee)
    {
        String[] names = objectName.split("\\.");
        if (names.length != 3)
            return false;

        if (findUserByName(grantee) == null)
            return false;

        Grant g = new Grant(names[0], names[1], names[2], grantee, "SELECT");
        Grant e = findGrant(g);
        if (e == null) {
            grantInfo.add(g);
            return true;
        } else {
            return false;
        }
    }

    boolean revokeSelect(String objectName, String revokee)
    {
        String[] names = objectName.split("\\.");
        if (names.length != 3)
            return false;

        if (findUserByName(revokee) == null)
            return false;

        Grant g = new Grant(names[0], names[1], names[2], revokee, "SELECT");
        Grant e = findGrant(g);
        if (e == null) {
            return false;
        } else {
            grantInfo.remove(e);
            return true;
        }
    }

    @Override
    public ResultSet getCatalogs() throws SQLException
    {
        List<OctopusMockupTuple> ts = new ArrayList<>();
        for (Datasource ds : metaInfo) {
            List<String> values = new ArrayList<>();
            values.add(ds.datasourceName);
            values.add(ds.comment);

            ts.add(new OctopusMockupTuple(values));
        }
        Collections.sort(ts, new Comparator<OctopusMockupTuple>() {
            @Override
            public int compare(OctopusMockupTuple tl, OctopusMockupTuple tr)
            {
                return tl.get(0).compareTo(tr.get(0));
            }
        });

        OctopusMockupResultSet rs = new OctopusMockupResultSet();
        rs.addTuples(ts);
        return rs;
    }

    private class SchemaComparator implements Comparator<OctopusMockupTuple>
    {
        @Override
        public int compare(OctopusMockupTuple tl, OctopusMockupTuple tr)
        {
            int r = tl.get(1).compareTo(tr.get(1));
            if (r == 0) {
                return tl.get(0).compareTo(tr.get(0));
            } else {
                return r;
            }
        }
    }

    @Override
    public ResultSet getSchemas() throws SQLException
    {
        List<OctopusMockupTuple> ts = new ArrayList<>();
        for (Datasource ds : metaInfo){
            for(Schema schema : ds.schemas) {
                List<String> values = new ArrayList<>();
                values.add(schema.schemaName);
                values.add(ds.datasourceName);
                values.add(schema.comment);

                ts.add(new OctopusMockupTuple(values));
            }
        }
        Collections.sort(ts, new SchemaComparator());

        OctopusMockupResultSet rs = new OctopusMockupResultSet();
        rs.addTuples(ts);
        return rs;
    }

    @Override
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException
    {
        Datasource ds = null;
        for (Datasource tmp : metaInfo) {
            if (tmp.datasourceName.equals(catalog)) {
                ds = tmp;
                break;
            }
        }

        if (ds == null)
            return new OctopusMockupResultSet();

        List<OctopusMockupTuple> ts = new ArrayList<>();
        final String pattern = convertPattern(schemaPattern);
        for (Schema schema : ds.schemas) {
            if (!schema.schemaName.matches(pattern))
                continue;

            List<String> values = new ArrayList<>();
            values.add(schema.schemaName);
            values.add(ds.datasourceName);
            values.add(schema.comment);

            ts.add(new OctopusMockupTuple(values));
        }
        Collections.sort(ts, new SchemaComparator());

        OctopusMockupResultSet rs = new OctopusMockupResultSet();
        rs.addTuples(ts);
        return rs;
    }

    private class TableComparator implements Comparator<OctopusMockupTuple>
    {
        @Override
        public int compare(OctopusMockupTuple tl, OctopusMockupTuple tr)
        {
            int r = tl.get(0).compareTo(tr.get(0));
            if (r != 0)
                return r;
            r = tl.get(1).compareTo(tr.get(1));
            if (r != 0)
                return r;
            return tl.get(2).compareTo(tr.get(2));
        }
    }

    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException
    {
        Datasource ds = null;
        for (Datasource tmp : metaInfo) {
            if (tmp.datasourceName.equals(catalog)) {
                ds = tmp;
                break;
            }
        }

        if (ds == null)
            return new OctopusMockupResultSet();

        List<OctopusMockupTuple> ts = new ArrayList<>();
        final String sPattern = convertPattern(schemaPattern);
        final String tPattern = convertPattern(tableNamePattern);
        for (Schema schema : ds.schemas) {
            if (!schema.schemaName.matches(sPattern))
                continue;

            for (Table table : schema.tables) {
                if (!table.tableName.matches(tPattern))
                    continue;

                List<String> values = new ArrayList<>();
                values.add(ds.datasourceName);
                values.add(schema.schemaName);
                values.add(table.tableName);
                values.add(table.type);
                values.add(table.comment);

                ts.add(new OctopusMockupTuple(values));
            }
        }
        Collections.sort(ts, new TableComparator());

        OctopusMockupResultSet rs = new OctopusMockupResultSet();
        rs.addTuples(ts);
        return rs;
    }

    @Override
    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException
    {
        List<OctopusMockupTuple> ts = new ArrayList<>();
        final String sPattern = convertPattern(schemaPattern);
        final String tPattern = convertPattern(tableNamePattern);
        for (Grant g : grantInfo) {
            if (!g.datasourceName.equals(catalog))
                continue;
            if (!g.schemaName.matches(sPattern))
                continue;
            if (!g.tableName.matches(tPattern))
                continue;

            List<String> values = new ArrayList<>();
            values.add(g.datasourceName);
            values.add(g.schemaName);
            values.add(g.tableName);
            values.add(g.grantor);
            values.add(g.grantee);
            values.add(g.privilege);

            ts.add(new OctopusMockupTuple(values));
        }
        Collections.sort(ts, new TableComparator());

        OctopusMockupResultSet rs = new OctopusMockupResultSet();
        rs.addTuples(ts);
        return rs;
    }

    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
    {
        Datasource ds = null;
        for (Datasource tmp : metaInfo) {
            if (tmp.datasourceName.equals(catalog)) {
                ds = tmp;
                break;
            }
        }

        if (ds == null)
            return new OctopusMockupResultSet();

        List<OctopusMockupTuple> ts = new ArrayList<>();
        final String sPattern = convertPattern(schemaPattern);
        final String tPattern = convertPattern(tableNamePattern);
        final String cPattern = convertPattern(columnNamePattern);
        for (Schema schema : ds.schemas) {
            if (!schema.schemaName.matches(sPattern))
                continue;

            for (Table table : schema.tables) {
                if (!table.tableName.matches(tPattern))
                    continue;

                for (Column col : table.columns) {
                    if (!col.columnName.matches(cPattern))
                        continue;

                    List<String> values = new ArrayList<>();
                    values.add(ds.datasourceName);
                    values.add(schema.schemaName);
                    values.add(table.tableName);
                    values.add(col.columnName);
                    values.add(col.type);
                    values.add(col.secuType);
                    values.add(col.comment);

                    ts.add(new OctopusMockupTuple(values));
                }
            }
        }
        Collections.sort(ts, new TableComparator());

        OctopusMockupResultSet rs = new OctopusMockupResultSet();
        rs.addTuples(ts);
        return rs;
    }

    private String convertPattern(final String pattern)
    {
        final char SEARCH_STRING_ESCAPE = '\\';

        if (pattern == null) {
            return ".*";
        } else {
            StringBuilder result = new StringBuilder(pattern.length());

            boolean escaped = false;
            for (int i = 0; i < pattern.length(); i++) {
                char c = pattern.charAt(i);
                if (escaped) {
                    if (c != SEARCH_STRING_ESCAPE)
                        escaped = false;
                    result.append(c);
                } else {
                    if (c == SEARCH_STRING_ESCAPE)
                        escaped = true;
                    else if (c == '%')
                        result.append(".*");
                    else if (c == '_')
                        result.append('.');
                    else
                        result.append(c);
                }
            }

            return result.toString();
        }
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException
    {
        if (clazz.isInstance(this))
            return clazz.cast(this);
        throw new ClassCastException("not a " + clazz);
    }
}
