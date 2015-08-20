package kr.co.bitnine.octopus.mockup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class OctopusMockupResultSet extends AbstractResultSet
{
    private final List<OctopusMockupTuple> tuples;
    private Iterator<OctopusMockupTuple> iter;
    private OctopusMockupTuple cursor;
    private boolean isClosed;

    OctopusMockupResultSet()
    {
        tuples = new ArrayList<>();
        iter = null;
        cursor = null;
        isClosed = false;
    }

    void addTuple(OctopusMockupTuple t)
    {
        tuples.add(t);
    }

    void addTuples(List<OctopusMockupTuple> ts)
    {
        tuples.addAll(ts);
    }

    @Override
    public boolean next() throws SQLException
    {
        if (iter == null)
             iter = tuples.iterator();

        if (iter.hasNext()) {
            cursor = iter.next();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void close() throws SQLException
    {
        isClosed = true;
    }

    @Override
    public String getString(int i) throws SQLException
    {
        if (isClosed)
            throw new SQLException("result set is already closed");
        if (cursor == null)
            throw new SQLException("cursor is not initialized");

        try {
            return cursor.get(i - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new SQLException("column index is not valid");
        }
    }
}
