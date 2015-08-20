package kr.co.bitnine.octopus.mockup;

public class Column
{
    String columnName;
    String type;
    String secuType;
    String comment;

    public Column(String columnName, String type, String secuType, String comment)
    {
        this.columnName = columnName;
        this.type = type;
        this.secuType = secuType;
        this.comment = comment;
    }
}
