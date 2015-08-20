package kr.co.bitnine.octopus.mockup;

import java.util.List;

public class Table
{
    String tableName;
    String type;
    String comment;
    List<Column> columns;

    public Table(String tableName, String type, String comment, List<Column> columns)
    {
        this.tableName = tableName;
        this.type = type;
        this.comment = comment;
        this.columns = columns;
    }
}
