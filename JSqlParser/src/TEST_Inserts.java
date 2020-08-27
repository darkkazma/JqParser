import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.List;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;

public class TEST_Inserts {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    new TEST_Inserts();
  }
  
  private ArrayList<String> columns = null;
  private ArrayList<String> values = null;
  
  public TEST_Inserts(){
    
    File qFile = new File("C:\\Users\\darkkazma\\desktop\\query.sql");
    BufferedReader br;
    try {
      br = new BufferedReader(new InputStreamReader(new FileInputStream(qFile)));
      String sql = br.readLine(); br.close();
      
      
       String Delete = "";
      String Where = "";
      int whereidx = sql.toUpperCase().indexOf("WHERE");
      if( whereidx != -1 ){
        Delete = sql.substring(0, (whereidx + 5) );
        Where = sql.substring(whereidx + 5).trim();
      }
      
      System.out.println("DELETE : ["+Delete+"] ");
      System.out.println("WHERE : ["+Where+"]");
      
      System.out.println( "COLUMN : " +  FindColumnName(Where, "REG_NAME"));
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  
  
  public String FindColumnName(String str, String column){
    String rtnVal = "";
    int idx = str.indexOf("\""+column+"\"");
    
    String new_where = str.substring(idx);
    if( idx > -1 ){
      // OR 나 AND 가 나오거나 둘다 없거나 
      int or_idx = new_where.toUpperCase().indexOf("\' OR \"");
      int and_idx = new_where.toUpperCase().indexOf("\' AND \"");
      int idx_s = 0;
      
      System.out.println("OR_IDX : ["+or_idx+"] ");
      System.out.println("AND_IDX : ["+and_idx+"] ");
      
      if( or_idx !=-1 || and_idx != -1 ){
        
        
        //OR이 오던지 AND가 오던지 암튼 작은거 -1이 아닌
        if( or_idx == - 1 ){ if( and_idx != -1 ){ idx_s = and_idx + idx + 1 ;} }
        if( and_idx == -1 ){ if( or_idx != -1 ){ idx_s = or_idx + idx + 1; } }
        if( idx_s < 1 ){
          idx_s = or_idx > and_idx ? and_idx : or_idx;
          idx_s += idx;
        }
      }
      else{ idx_s = str.length(); }
      rtnVal = str.substring(idx, idx_s);
    }else{
      return rtnVal;
    }
    return rtnVal;
  }
  
  
  // LONG 컬럼에 대한 제외 쿼리를 반환 한다.
  public String repactorySql(String str, String column){
    StringBuffer sqls = new StringBuffer();
    String first = "";
    String condition = "";
    String last = "";
    int idx = str.indexOf("\""+column+"\"");
    
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
  
//INSERT SQL문으로 부터 컬럼과 value값을 분리하여 저장한다.
 private void Parsing_Insert_Query(String data) throws Exception{
   
   try{
     columns = new ArrayList<String>();
     values = new ArrayList<String>();
     
     CCJSqlParserManager pm = new CCJSqlParserManager();
     Statement Insertst = pm.parse(new StringReader(data));
     
     List<Column> colist = ((Insert) Insertst).getColumns();
     ItemsList items = ((Insert) Insertst).getItemsList();
     List<Expression> expressions = ((ExpressionList) items).getExpressions();
     
     for(Column col : colist ){
       String column = col.getColumnName().replace("\"", "").trim();
       System.out.println("column : " + column);
       columns.add(column);
     }
     if( expressions != null && expressions.size() > 0 ){
       for( Expression exp : expressions){
         values.add( exp.toString() );
       }
     }
     
   }catch(Exception ea){
     System.err.println("@@ Insert Query Parsing Error : " + ea.toString());
     throw ea; 
   }
   
 }

}
