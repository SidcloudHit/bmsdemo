/**
 *
 * @author NLRMP
 */
package DBCon;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
//import java.util.Iterator;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


import com.sun.rowset.CachedRowSetImpl;
import javax.sql.rowset.CachedRowSet;


public final class DBConnection {
    private Connection _connection;
    private PreparedStatement _prepSt;
    private ResultSet _resultset;
    private SQLException _sqlException;
    private Savepoint _savepoint;
    private String _url;
    private String[] _dbProperties;
    private int _updateResult;
    private String _datasource;
    private CachedRowSet _rowset;
    private String _strSql;
    public DBConnection(String _url) {
        this._url = _url;
    }
    
    private String[] getInfo() {
        try {
            BufferedReader input = new BufferedReader(new FileReader(_url));
            _dbProperties = input.readLine().split(",");
           // _dbProperties[0]="jdbc:sqlserver://"+_dbProperties[0]+":1433";
            _dbProperties[0]="jdbc:postgresql://"+_dbProperties[0]+":5432/";
        } catch(FileNotFoundException fnfe) { throw new RuntimeException(fnfe); } catch(IOException ioe) { throw new RuntimeException(ioe); }
        return _dbProperties;
    }
    
    private void doRollback(Connection _connection, SQLException _sqle) {
        try {
            if(_connection!=null) {
                if(_savepoint != null) {
                    _connection.rollback(_savepoint);
                    _connection.releaseSavepoint(_savepoint);
                } else {
                    _connection.rollback();
                }
                _connection.setAutoCommit(true);
                _sqlException = _sqle;
            }
        } catch(SQLException sqle) { throw new RuntimeException(sqle); }
    }
    
    public DBCon.Connect connect() {
        return new DBCon.Connect() {
            private ArrayList _table = new ArrayList();
            private String[] _values;
            private String _value;
            private boolean _isTransaction;
            private int _executionCount, COUNT;
            
            public Connection getConnection() {
                try {
                    if(_url.length()<1) {
                        _connection =retConnection(_datasource);
                        
                    } else {
                        _dbProperties = getInfo();
                     //  DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
                        // DriverManager.registerDriver(new org.postgresql.Driver());
                       // _connection = DriverManager.getConnection(_dbProperties[0]+";databaseName="+_dbProperties[1]+";user="+_dbProperties[2]+";password="+_dbProperties[3]);
                         _connection = DriverManager.getConnection(_dbProperties[0]+_dbProperties[1],_dbProperties[2],_dbProperties[3]);
                    }
                } catch(SQLException sqle) { throw new RuntimeException(sqle); }
                return _connection;
            }
            
            public void closeAll(ResultSet _resultSet, PreparedStatement _prepSt, Connection _connection) {
                try {
                    if(_resultSet!=null) _resultSet.close();
                    if(_prepSt!=null) {
                        _prepSt.clearParameters();
                        _prepSt.close();
                    }
                    if(_connection!=null) _connection.close();
                } catch(SQLException sqle) { throw new RuntimeException(sqle); }
            }
            
            public void closePreparedStatement(PreparedStatement _prepSt) {
                try {
                    if(_prepSt!=null) {
                        _prepSt.clearParameters();
                        _prepSt.close();
                    }
                } catch(SQLException sqle) { throw new RuntimeException(sqle); }
            }
            
            public void closeConnection(Connection _connection) {
                try {
                    if(_connection!=null) _connection.close();
                } catch(SQLException sqle) { throw new RuntimeException(sqle); }
            }
            
            public void doTransaction(int _execCount) {
                try {
                    _executionCount = _execCount;
                    _isTransaction = true;
                    if(_connection==null){
                        _connection = getConnection();
                    }
                    if (_connection.getAutoCommit()==true) {
                        _connection.setAutoCommit(false);
                    }
                } catch(SQLException sqle) { throw new RuntimeException(sqle); }
            }
            
            public void setSavepoint(String _savepointName) {
                try {
                    if(_connection==null){
                        _connection = getConnection();
                    }
                    if (_connection.getAutoCommit()==true) {
                        _connection.setAutoCommit(false);
                    }
                    _savepoint = _connection.setSavepoint(_savepointName);
                } catch(SQLException sqle) { throw new RuntimeException(sqle); }
            }
            
            public void setQuery(String _query) {
                try {
                    if(_isTransaction==false) _connection = getConnection();
                    _prepSt = _connection.prepareStatement(_query);
                    _strSql=_query;
                } catch(SQLException sqle) { throw new RuntimeException(sqle); }
            }
            
            public String[] executeToColumnNames() {
                try {
                    _resultset = _prepSt.executeQuery();
                    int bound = _resultset.getMetaData().getColumnCount();
                    _values = null;
                    if(_resultset.next()) {
                        _values = new String[bound];
                        for(int i=1;i<=bound;i++) _values[i-1] = _resultset.getMetaData().getColumnName(i);
                    }
                    if(_isTransaction==true) COUNT = COUNT + 1;
                } catch(SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                } finally {
                    if(_isTransaction==true) {
                        if(_executionCount == COUNT) {
                            setDefault();
                            closeAll(_resultset,_prepSt,_connection);
                        }
                    } else {
                        closeAll(_resultset,_prepSt,_connection);
                    }
                }
                return _values;
            }
            
            public String executeToValue() {
                try {
                    _resultset = _prepSt.executeQuery();
                    _value = null;
                    if(_resultset.next()) {
                        if(_resultset.getMetaData().getColumnTypeName(1).equals("bytea")) _value = new String(_resultset.getBytes(1));
                        else _value = _resultset.getString(1);
                    }
                    if(_isTransaction==true) COUNT = COUNT + 1;
                } catch(SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                } finally {
                    if(_isTransaction==true) {
                        if(_executionCount == COUNT) {
                            setDefault();
                            closeAll(_resultset,_prepSt,_connection);
                        }
                    } else {
                        closeAll(_resultset,_prepSt,_connection);
                    }
                }
                return _value;
            }
            
            public String[] executeToRow() {
                try {
                    _resultset = _prepSt.executeQuery();
                    int bound = _resultset.getMetaData().getColumnCount();
                    _values = null;
                    if(_resultset.next()) {
                        _values = new String[bound];
                        for(int i=1;i<=bound;i++) {
                            if(_resultset.getMetaData().getColumnTypeName(i).equals("bytea")) _values[i-1] = new String(_resultset.getBytes(i));
                            else _values[i-1] = _resultset.getString(i);
                        }
                    }
                    if(_isTransaction==true) COUNT = COUNT + 1;
                } catch(SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                } finally {
                    if(_isTransaction==true) {
                        if(_executionCount == COUNT) {
                            setDefault();
                            closeAll(_resultset,_prepSt,_connection);
                        }
                    } else {
                        closeAll(_resultset,_prepSt,_connection);
                    }
                }
                
                return _values;
            }
            
            public ArrayList executeToRows() {
                try {
                    _resultset = _prepSt.executeQuery();
                    int _bound = _resultset.getMetaData().getColumnCount();
                    _table.clear();
                    while(_resultset.next()) {
                        _values = new String[_bound];
                        for(int i=1;i<=_bound;i++) {
                            if(_resultset.getMetaData().getColumnTypeName(i).equals("bytea")) _values[i-1] = new String(_resultset.getBytes(i));
                            else _values[i-1] = _resultset.getString(i);
                        }
                        _table.add(_values);
                    }
                    if(_isTransaction==true) COUNT = COUNT + 1;
                } catch(SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                } finally {
                    if(_isTransaction==true) {
                        if(_executionCount == COUNT) {
                            setDefault();
                            closeAll(_resultset,_prepSt,_connection);
                        }
                    } else {
                        closeAll(_resultset,_prepSt,_connection);
                    }
                }
                return _table;
            }
            
            public ArrayList executeToDDL() {
                try {
                    _resultset = _prepSt.executeQuery();
                    int _count = 0; _table.clear();
                    while(_resultset.next())
                        _table.add(_count++, new OptionItem(_resultset.getString(1),_resultset.getString(2)));
                } catch(SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                } finally {
                    closeAll(_resultset,_prepSt,_connection);
                }
                return _table;
            }
            
            public void setParameter(int _index, String _value) {
                try {
                    _prepSt.setString(_index, _value);
                } catch (SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                }
            }
            
            public void setParameter(int _index, int _value) {
                try {
                    _prepSt.setInt(_index, _value);
                } catch (SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                }
            }
            
            public void setParameter(int _index, long _value) {
                try {
                    _prepSt.setLong(_index, _value);
                } catch (SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                }
            }
            public void setParameter(int _index, boolean _value) {
                try {
                    _prepSt.setBoolean(_index, _value);
                } catch (SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                }
            }
            
            public void setParameter(int _index, byte[] _value) {
                try {
                    _prepSt.setBytes(_index, _value);
                } catch (SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                }
            }
            
            public void setParameter(int _index, float _value) {
                try {
                    _prepSt.setFloat(_index, _value);
                } catch (SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                }
            }
            
            public void setParameter(int _index, double _value) {
                try {
                    _prepSt.setDouble(_index, _value);
                } catch (SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                }
            }
            
            public void setNull(int _index, int _sqlType) {
                try {
                    _prepSt.setNull(_index, _sqlType);
                } catch (SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                }
            }
            
            public void setParameter(int _index, java.sql.Date _value) {
                try {
                    _prepSt.setDate(_index, _value);
                } catch (SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                }
            }
            
            public void setParameter(int _index, java.sql.Time _value) {
                try {
                    _prepSt.setTime(_index, _value);
                } catch (SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                }
            }
            
            public void setParameter(int _index, java.sql.Timestamp _value) {
                try {
                    _prepSt.setTimestamp(_index, _value);
                } catch (SQLException sqle) {
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                }
            }
            
            public int executeUpdate() {
                try {
                    _updateResult = _prepSt.executeUpdate();
                    if(_isTransaction==true) {
                        COUNT = COUNT + 1;
                        if(_executionCount == COUNT) doCommit();
                    }
                } catch(SQLException sqle) {
                    if(_isTransaction==true) doRollback(_connection, sqle);
                    closeAll(_resultset,_prepSt,_connection);
                    throw new RuntimeException(sqle);
                } finally {
                    if(_isTransaction==true) {
                        if(_executionCount == COUNT) {
                            setDefault();
                            closeAll(_resultset,_prepSt,_connection);
                        }
                    } else {
                        closeAll(_resultset,_prepSt,_connection);
                    }
                }
                return _updateResult;
            }
            
            
            public CachedRowSet getRowSet() {
                try {
                    //_resultset=_prepSt.executeQuery();
                    CachedRowSetImpl crs=new CachedRowSetImpl();
                    crs.populate(_prepSt.executeQuery());
                    return crs;
                } catch (SQLException ex) {
                    closeAll(_resultset,_prepSt,_connection);
                    ex.printStackTrace();
                } finally{
                    if(_isTransaction==true) {
                        if(_executionCount == COUNT) {
                            setDefault();
                            closeAll(_resultset,_prepSt,_connection);
                        }
                    } else {
                        closeAll(_resultset,_prepSt,_connection);
                    }
                }
                return null;
                
            }
            
            public CachedRowSet getRowSet(int rps,int psz) {
                try {
                    java.sql.PreparedStatement  pstmt;
                    pstmt=_connection.prepareStatement(_strSql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                    CachedRowSetImpl crs=new CachedRowSetImpl();
                    crs.setPageSize(psz);
                    crs.populate(pstmt.executeQuery(),rps);
                    return crs;
                } catch (SQLException ex) {
                    setDefault();
                    closeAll(_resultset,_prepSt,_connection);
                    ex.printStackTrace();
                } finally{
                    if(_isTransaction==true) {
                        if(_executionCount == COUNT) {
                            setDefault();
                            closeAll(_resultset,_prepSt,_connection);
                        }
                    } else {
                        closeAll(_resultset,_prepSt,_connection);
                    }
                }
                return null;
                
            }
            
            public CachedRowSet getRowSet(int psz) {
                try {
                    //java.sql.Statement stmt;
                    //_connection.setAutoCommit(false);
                    //stmt = _connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    //stmt=_connection.createStatement();
                    //_resultset=stmt.executeQuery(_strSql);
                    //_resultset=_prepSt.executeQuery();
                    CachedRowSetImpl crs=new CachedRowSetImpl();
                    crs.setPageSize(psz);
                    crs.setCommand(_strSql);
                    crs.execute(_connection);
                    //crs.populate(_resultset,rps);
                    return crs;
                } catch (SQLException ex) {
                    setDefault();
                    closeAll(_resultset,_prepSt,_connection);
                    ex.printStackTrace();
                } finally{
                    if(_isTransaction==true) {
                        if(_executionCount == COUNT) {
                            setDefault();
                            closeAll(_resultset,_prepSt,_connection);
                        }
                    } else {
                        closeAll(_resultset,_prepSt,_connection);
                    }
                }
                return null;
                
            }
            
            private void doCommit() {
                try {
                    if(_connection!=null) {
                        if(_savepoint != null) {
                            _connection.releaseSavepoint(_savepoint);
                        }
                        _connection.commit();
                    }
                } catch(SQLException sqle) { throw new RuntimeException(sqle); }
            }
            private void setDefault() {
                try {
                    _isTransaction = false;
                    _sqlException = null;
                    _savepoint = null;
                    _executionCount = 0;
                    COUNT = 0;
                    if(_connection.getAutoCommit()==false)  _connection.setAutoCommit(true);
                } catch (SQLException sqle) { throw new RuntimeException(sqle); }
            }
        };
    }
    
//@Override
    public void finalize() {
        System.gc();
    }
    
    public String getDatasource() {
        return _datasource;
    }
    
    public void setDatasource(String datasource) {
        this._datasource = datasource;
    }
    private java.sql.Connection retConnection(String dsName) {
        dsName="java:/comp/env/" + dsName;
        try {
            InitialContext ctx;
            
            ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(dsName);
            if ( ds == null ) {
  //throw new Exception("Data source not found!");
                int i=0;
            }
            try {
                return ds.getConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
    
}
