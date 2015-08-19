package kr.co.bitnine.octopus.mockup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OctopusMockupResultSet {
    class Tuple extends ArrayList<String> {
        public Tuple(List<String> values) { super(values);}
    }

    protected int curRow;
    protected long totalRow;
    protected boolean wasNull;
    protected Tuple cur;
    List<Tuple> tuples;


    public OctopusMockupResultSet(){
        cur = null;
        curRow = 0;
        totalRow = 0;
        wasNull = false;
        tuples = new ArrayList<Tuple>();
    }

    public void addTuple(List<String> values){
        Tuple tuple = new Tuple(values);
        tuples.add(tuple);
        totalRow++;
    }

    public String getString(int i) throws SQLException{
        String value = cur.get(i - 1);
        return value;
    }

    public boolean next() throws SQLException {
        if (totalRow <= 0) {
            return false;
        }

        cur = nextTuple();
        curRow++;
        if (cur != null) {
            return true;
        }
        return false;
    }

    protected Tuple nextTuple() {
        if(curRow >= totalRow) {
            return null;
        }
        return tuples.get(curRow);
    }
}
