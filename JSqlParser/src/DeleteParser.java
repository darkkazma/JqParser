import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.First;

public class DeleteParser {

  public static void main(String[] args){
    new DeleteParser();
  }
  
  public DeleteParser(){
    
    ArrayList<String> longColumn = new ArrayList<String>();
    //longColumn.add("\"TEST_COLUMN_02\"");
    //longColumn.add("\"TEST_COLUMN_03\"");
    
    try{
      File qFile = new File("C:\\Users\\darkkazma\\desktop\\test.sql");
      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(qFile)));
      String sql = br.readLine(); br.close();
      
      String delete = "";
      String where = "";
      
      int whereidx = sql.toUpperCase().indexOf("WHERE");
      if( whereidx != -1 ){
        delete = sql.substring(0, whereidx + 5);
        where = sql.substring(whereidx + 5).trim();
        // where 이후 AND 로 구분한다.
        for(String lColumn : longColumn){
          where = repactorySql(where, lColumn);
        }
      }
      
      System.out.println(delete);
      System.out.println("----------------------------------------------------------------------------");
      System.out.println(delete + " " + where);
      
      
      /*
      System.out.println(sql);
      
      Connection con = OracleDBConMng.getInstance().getConnection("192.168.0.186", 1521, "TESTBCF", "system", "oracle");
      PreparedStatement pstmt = con.prepareStatement(sql);
      System.out.println("Execute Result : [" +  pstmt.executeUpdate() +"]" );
      pstmt.close(); pstmt = null;
      */
      
      
      
      
    }catch(Exception ea){
      ea.printStackTrace();
    }
  }
  
  public String repactorySql(String str, String column){
    StringBuffer sqls = new StringBuffer();
    String first = "";
    String condition = "";
    String last = "";
    int idx = str.indexOf(column);
    
    if( idx > 0 ){
      condition = str.substring(idx);
      int andor_idx = 0;
      
      for( int i=(idx -1); i >= 0; i-- ){
        
        if( ( str.charAt(i) != ' ' && str.charAt(i) == 'd' ) 
            || (str.charAt(i) != ' ' && str.charAt(i) == 'D') ){ andor_idx = idx -4; }
        if( ( str.charAt(i) != ' ' && str.charAt(i) == 'r' ) 
            || (str.charAt(i) != ' ' && str.charAt(i) == 'R') ){ andor_idx = idx -3; }
      }
      first = str.substring(0, andor_idx);
      condition = str.substring(idx);
      
      int lastidx = condition.trim().toUpperCase().indexOf(" AND "); 
      if(  lastidx != -1 ){
        
        if( first.length() == 0 ){
          lastidx = lastidx + 5;
        }
        last = condition.trim().substring(lastidx);
        condition = condition.trim().substring(0, lastidx);
      }
    }else{ sqls.append(str); }
    
    if( first.length() > 0 ){ sqls.append(first); }
    if( last.length() > 0 ){ sqls.append(last); }
    
    return sqls.toString();
  }
  
  // AND 와 OR로 구분하여 회귀 한다.
  public String ParsingWhere(String str, String delim){
    String rtnData = "";
    
    int count = 0;
    StringTokenizer st = new StringTokenizer(str.toUpperCase(), delim);
    while( st.hasMoreTokens() ){
      String data = st.nextToken();
      rtnData += " " + delim + " " + data;
      count ++;
    }
    
    if( count == 0){ return null; }
    return "";
  }
  
}
