package kr.co.bitnine.octopus.mockup;

import java.util.List;

public class Datasource
{
    String datasourceName;
    String comment;
    List<Schema> schemas;

    public Datasource(String datasourceName, String comment, List<Schema> schemas)
    {
        this.datasourceName = datasourceName;
        this.comment = comment;
        this.schemas = schemas;
    }
}

