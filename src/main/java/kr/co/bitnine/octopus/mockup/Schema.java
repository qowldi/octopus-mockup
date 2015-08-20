package kr.co.bitnine.octopus.mockup;

import java.util.List;

public class Schema
{
    String schemaName;
    String comment;
    List<Table> tables;

    public Schema(String schemaName, String comment, List<Table> tables)
    {
        this.schemaName = schemaName;
        this.comment = comment;
        this.tables = tables;
    }
}
