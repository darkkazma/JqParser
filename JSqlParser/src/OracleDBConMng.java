

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

public class OracleDBConMng {

  private ArrayList<ConnectionObject> connections = new ArrayList<ConnectionObject>(10);
  
  //드라이버 class
  private String _driver = "oracle.jdbc.driver.OracleDriver";
  private String _url = "";
  private String _user = "";
  private String _pwd = "";
  
  private boolean _traceOn = false;
  private boolean initialized = false;
  private int _openConnections = 10;
  private static OracleDBConMng instance = null;
  
  public OracleDBConMng(){}
  
  public static OracleDBConMng getInstance(){
    if( instance == null ){
      synchronized( OracleDBConMng.class ){
        if( instance == null ){
          instance = new OracleDBConMng();
        }
      }
    }
    return instance;
  }
  
  
  public synchronized Connection getConnection(String sourceIP, int sourcePort, String SID, String user, String pwd ) throws Exception{
    if( !initialized ){
      Class<?> c = Class.forName(_driver);
      DriverManager.registerDriver( (Driver) c.newInstance() );
      Locale.setDefault(Locale.US);
      initialized = true;
    }
    
    _url = "jdbc:oracle:thin:@"+sourceIP+":"+sourcePort+":"+SID;
    _user = user;
    _pwd = pwd;
        
    Connection c = null;
    ConnectionObject co = null;
    boolean badConnection = false;
    
    for( int i=0; i<connections.size(); i++){
      co = (ConnectionObject) connections.get(i);
      if( _url.equals(co._url) && _user.equals(co._user) && _pwd.equals(co._pwd)){
        if( !co.inUse ){
          try{ 
            badConnection = co.connection.isClosed();
            if( !badConnection )// 에러가 발생했다면 true 설정
              badConnection = (co.connection.getWarnings() != null);
          }catch(Exception e){
            badConnection = true;
            System.err.println(e.getMessage());
          }
          
          if( badConnection ){
            connections.remove(i);
            trace("OracleDBConMng : remove disconnected DB connection #" + i);
            continue;
          }
          c = co.connection;
          co.inUse = true;
          System.out.println("OracleDBConMng : Using existing DB connection #" + (i+1) );
          break;
        }
        
        System.out.println("OracleDBConMng : Using existing DB connection #" + (i+1));
        break;
      }
    }
      
    // 처음 접속자는 무조건 null
    if( c == null ){
      c = createConnection();
      co = new ConnectionObject(c, true, _url, user, pwd);
      connections.add(co);
      trace ("OracleDBConMng : Creating new DB connection #" + connections.size());
      System.out.println("OracleDBConMng : Creating new DB connection #" + connections.size());
    }
    
    return c;
  }
  
  /**
   * 실제 커넥션을 만들어서 돌려준다.
   * @return
   * @throws SQLException
   */
  private Connection createConnection() throws SQLException{
    Connection con = null;
    try{
      System.out.println("## URL : ["+_url+"]");
      Properties info = new Properties();
      info.put("user", _user);
      info.put("password", _pwd);
      info.put("internal_logon", "sysdba");
      con = DriverManager.getConnection(_url, info);
      //con = DriverManager.getConnection(_url, _user, _pwd);
    }catch(Throwable t){ throw new SQLException(t.getMessage()); }
    return con;
  }
  
  /**
   * 커넥션을 삭제 한다.
   * @param c
   */
  public synchronized void removeConnection(Connection c){
    if( c == null ) return;
    ConnectionObject co = null;
    for( int i=0; i<connections.size(); i++){
      co = (ConnectionObject) connections.get(i);
      if( c == co.connection ){
        try{
          c.close();
          connections.remove(i);
        }catch(Exception e){
          System.err.println(e.getMessage());
        }
        break;
      }
    }
  }
  
  /**
   * 사용하지 않는 Connection을 비운다.
   */
  public void releaseFreeConnections(){
    trace("OracleDBConMng.releaseFreeConnections()");
    ConnectionObject co = null;
    for( int i=0; i< connections.size(); i++){
      co = (ConnectionObject) connections.get(i);
      if( !co.inUse ){  
        removeConnection(co.connection);
      }
    }
  }
  
  public void finalize(){
    ConnectionObject co = null;
    for( int i=0; i< connections.size(); i++){
      co = (ConnectionObject) connections.get(i);
      try{
        co.connection.close();
      }catch(Exception e){
        System.err.println(e.getMessage());
      }
      co = null;
    }
    connections.clear();
  }
  
  public synchronized void freeConnection(Connection c){
    if( c == null ) return;
    ConnectionObject co = null;
    
    // Client가 사용한 Connection 객체를 찾는다.
    for( int i=0; i<connections.size(); i++ ){
      co = (ConnectionObject) connections.get(i);
      if( c == co.connection ){
        co.inUse = false;
        break;
      }
    }
    
    for( int i=0; i<connections.size(); i++ ){
      co = (ConnectionObject) connections.get(i);
      if( (i+1) > _openConnections && !co.inUse ){
        removeConnection(co.connection);
      }
    }
  }
  
  
  public void freeConnection(Connection c, PreparedStatement p, ResultSet r) {
    try {
        if (r != null) r.close();
        if (p != null) p.close();
        freeConnection(c);
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }
  }
  public void freeConnection(Connection c, Statement s, ResultSet r) {
      try {
          if (r != null) r.close();
          if (s != null) s.close();
          freeConnection(c);
      } catch (SQLException e) {
          System.err.println(e.getMessage());
      }
  }
  public void freeConnection(Connection c, PreparedStatement p) {
      try {
          if (p != null) p.close();
          freeConnection(c);
      } catch (SQLException e) {
          System.err.println(e.getMessage());
      }
  }
  public void freeConnection(Connection c, Statement s) {
      try {
          if (s != null) s.close();
          freeConnection(c);
      } catch (SQLException e) {
          System.err.println(e.getMessage());
      }
  }
  
  
  /**
   * 에러를 출력 한다.
   * @param s
   */
  private void trace(String s){
    if( _traceOn ){ System.err.println("@@ "+ s); }
  }
  
  
  
  public ArrayList<ConnectionObject> getConnections() {
    return connections;
  }

  public void setConnections(ArrayList<ConnectionObject> connections) {
    this.connections = connections;
  }

  public String get_driver() {
    return _driver;
  }

  public void set_driver(String _driver) {
    this._driver = _driver;
  }

  public String get_url() {
    return _url;
  }

  public void set_url(String _url) {
    this._url = _url;
  }

  public boolean is_traceOn() {
    return _traceOn;
  }

  public void set_traceOn(boolean _traceOn) {
    this._traceOn = _traceOn;
  }

  public int get_openConnections() {
    return _openConnections;
  }

  public void set_openConnections(int _openConnections) {
    this._openConnections = _openConnections;
  }








  class ConnectionObject{
    public Connection connection = null;
    public boolean inUse = false;   // Connection의 사용 여부
    public String _url;
    public String _user;
    public String _pwd;
    public ConnectionObject( Connection c, boolean useFlag, String url, String user, String pwd){
      connection = c;
      inUse = useFlag;
      _url = url;
      _user = user;
      _pwd = pwd;
    }
  }
}

