/**
 *
 * @author NLRMP
 */
package DBCon;

public interface Connect {
    /**
     * This function attempts to establish a connection to the database.
     * @return This function returns a connection to established to the appropriate driver.
     */
    public java.sql.Connection getConnection();
    /**
     * This function attempts to realease all the specified parameters by closing them explicitly.
     * @param _resultSet This parameter denotes a reference to the ResultSet which is to be closed.
     * @param _prepSt This parameter denotes a reference to the PreparedStatement which is to be closed.
     * @param _connection This parameter denotes a reference to the Connection which is to be closed.
     */
    public void closeAll(java.sql.ResultSet _resultSet, java.sql.PreparedStatement _prepSt, java.sql.Connection _connection);
    /**
     * This function attempts to realease the specified PreparedStatement parameter by closing it explicitly.
     * @param _prepSt This parameter denotes a reference to the PreparedStatement which is to be closed.
     */
    public void closePreparedStatement(java.sql.PreparedStatement _prepSt);
    /**
     * This function attempts to realease the specified Connection parameter by closing it explicitly.
     * @param _connection This parameter denotes a reference to the Connection which is to be closed.
     */
    public void closeConnection(java.sql.Connection _connection);
    /**
     * This function applies to transactions to be processed.
     * @param _queryCount This parameter denotes the total number of executions of queries.
     */
    public void doTransaction(int _executionCount);
    /**
     * This function creates a savepoint with the given name in the current transaction and returns the new savepoint object which represents it.
     * @param _savepointName This parameter denotes the savepoint name of the current transaction.
     */
    public void setSavepoint(String _savepointName);
    /**
     * This function applies to any kind of queries to be processed.
     * @param _query This parameter can be any of the SQL types viz., SELECT, INSERT, UPDATE or DELETE.
     */
    public void setQuery(String _query);
    /**
     * This function applies to setting a string type parameter to the query.
     * @param _index This parameter denotes the index of the current parameter to be set in the query.
     * @param _value This parameter denotes a string value to the index declared.
     */
    public void setParameter(int _index, String _value);
    /**
     * This function applies to setting an integer type parameter to the query.
     * @param _index This parameter denotes the index of the parameter of the query e.g., first parameter is 1, second is 2 and so on.
     * @param _value This parameter denotes an integer value to the index declared.
     */
    public void setParameter(int _index, int _value);
     /**
     * This function applies to setting an long type parameter to the query.
     * @param _index This parameter denotes the index of the parameter of the query e.g., first parameter is 1, second is 2 and so on.
     * @param _value This parameter denotes an integer value to the index declared.
     */
    public void setParameter(int _index, long _value);
    /**
     * This function applies to setting a boolean type parameter to the query.
     * @param _index This parameter denotes the index of the parameter of the query e.g., first parameter is 1, second is 2 and so on.
     * @param _value This parameter denotes a boolean value to the index declared.
     */
    public void setParameter(int _index, boolean _value);
    /**
     * This function applies to setting an array of bytes type parameter to the query.
     * @param _index This parameter denotes the index of the parameter of the query e.g., first parameter is 1, second is 2 and so on.
     * @param _value This parameter denotes an array of byte value to the index declared.
     */
    public void setParameter(int _index, byte[] _value);
    /**
     * This function applies to setting a float type parameter to the query.
     * @param _index This parameter denotes the index of the parameter of the query e.g., first parameter is 1, second is 2 and so on.
     * @param _value This parameter denotes a float value to the index declared.
     */
    public void setParameter(int _index, float _value);
    /**
     * This function applies to setting a double type parameter to the query.
     * @param _index This parameter denotes the index of the parameter of the query e.g., first parameter is 1, second is 2 and so on.
     * @param _value This parameter denotes a double value to the index declared.
     */
    public void setParameter(int _index, double _value);
    /**
     * This function applies to setting a java.sql.Date type parameter to the query.
     * @param _index This parameter denotes the index of the parameter of the query e.g., first parameter is 1, second is 2 and so on.
     * @param _value This parameter denotes a java.sql.Date value to the index declared.
     */
    public void setParameter(int _index, java.sql.Date _value);
    /**
     * This function applies to setting a java.sql.Time type parameter to the query.
     * @param _index This parameter denotes the index of the parameter of the query e.g., first parameter is 1, second is 2 and so on.
     * @param _value This parameter denotes a java.sql.Time value to the index declared.
     */
    public void setParameter(int _index, java.sql.Time _value);
    /**
     * This function applies to setting a java.sql.Timestamp type parameter to the query.
     * @param _index This parameter denotes the index of the parameter of the query e.g., first parameter is 1, second is 2 and so on.
     * @param _value This parameter denotes a java.sql.Timestamp value to the index declared.
     */
    public void setParameter(int _index, java.sql.Timestamp _value);
    /**
     * This function applies to setting a null of java.sql.Types type parameter to the query.
     * @param _index This parameter denotes the index of the parameter of the query e.g., first parameter is 1, second is 2 and so on.
     * @param _value This parameter denotes a java.sql.Types value to the index declared.
     */
    public void setNull(int _index, int _sqlType);
    /**
     * This function applies to only SELECT queries i.e., this doesn't apply to INSERT, UPDATE or DELETE queries.
     * @return This function returns an array of strings, each value of which holds the column name of the table.
     * For example,
     *     For a query like "SELECT field1, field2, field3 FROM table where.....", it'll return the names of all the columns.
     */
    public String[] executeToColumnNames();
    /**
     * This function applies to only SELECT queries i.e., this doesn't apply to INSERT, UPDATE or DELETE queries.
     * @return This function returns a string type value.
     * For example,
     *    (a) for queries like "SELECT field FROM table where.....", it'll return the value of the field, the value being a string type.
     *    (b) for queries like "SELECT field1, field2 FROM table where.....", it'll return the value value of the first field, the value being a string type.
     */
    public String executeToValue();
    /**
     * This function applies to only SELECT queries i.e., this doesn't apply to INSERT, UPDATE or DELETE queries.
     * @return This function returns an array of strings, each of which holds the value of a field of the table.
     * For example, for a query like "SELECT field1, field2, field3 FROM table where....."
     *    (a) If the query gives result to a single row of values, it'll return the values of the single row, each value being a string type.
     *    (b) If the query gives result to multiple rows of values, it'll return the values of the last row, each value being a string type.
     */
    public String[] executeToRow();
    /**
     * This function applies to only SELECT queries i.e., this doesn't apply to INSERT, UPDATE or DELETE queries.
     * @return This function returns an arraylist, each value being an array of strings holding a row of elements.
     * For example, for a query like "SELECT field1, field2, field3 FROM table where....."
     *    (a) If the query gives result to a single row of values, it'll return the values of the single row, each value being a string type.
     *    (b) If the query gives result to multiple rows of values, it'll return the values of all rows, each value being a string type.
     */
    public java.util.ArrayList executeToRows();
    /**
     * This function applies to only SELECT queries i.e., this doesn't apply to INSERT, UPDATE or DELETE queries.
     * This function is mainly used to fill values in a drop-down list which may either be of HTML or Struts framework.
     * @return This function returns an arraylist, each value being an object of OptionItem Class, which in turn retruns two values as <i>id</i> and <i>label</i>.
     * This function should take two fields in it's query, such that the first field is to be set as the <i>id</i> and the second field as the <i>label</i>.
     * For example,
     *    (a) for queries like "SELECT field1, field2 FROM table where.....", it'll set field1 as <i>id</i> and field2 as <i>label</i>.
     *    (b) for queries like "SELECT field1, field2, field3 FROM table where.....", it'll set field1 as <i>id</i> and field2 as <i>label</i> and will ignore other fields.
     */
    public java.util.ArrayList executeToDDL();
    /**
     * This function applies to only INSERT, UPDATE or DELETE queries i.e., this doesn't apply to SELECT queries.
     * @return This function returns the total number of inserted, updated or deleted rows.
     */
    public int executeUpdate();
    
    public javax.sql.rowset.CachedRowSet getRowSet();
    
    public javax.sql.rowset.CachedRowSet getRowSet(int rowPos,int pageSize);
   
    public javax.sql.rowset.CachedRowSet getRowSet(int pageSize);
   //public void doCommit();
}
