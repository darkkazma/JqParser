
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;


public class Parser {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    new Parser();
  }
  
  public Parser(){
    try{
      File qFile = new File("C:\\Users\\darkkazma\\desktop\\query.sql");
      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(qFile)));
      String sql = br.readLine(); br.close();
      
      //parsing_process(sql);
      
      CCJSqlParserManager pm = new CCJSqlParserManager();
      Statement stmt = pm.parse(new StringReader(sql));
      
      if( stmt instanceof Insert ){
        
      }
      
    }catch(Exception ea){ ea.printStackTrace(); }
  }
  
  public void parsing_process(String query){
    final String INSERT_INTO = "insert into";
    final int INSERT_INTO_LENGTH = INSERT_INTO.length();
    
    // INSERT INTO 제거
    query = query.trim();
    int ino_idx = query.toLowerCase().indexOf("insert into");
    query = query.substring(ino_idx + INSERT_INTO_LENGTH).trim();
    System.out.println(query);
    
    // 스키마/테이블
    // (로 시작해서 )로 끝나는 구문
    int col_start_idx = query.indexOf("(");
    int col_end_idx = query.indexOf(")");
    String col = query.substring(col_start_idx + 1, col_end_idx);
    System.out.println(col);
    
    query = query.substring(col_end_idx + 1);
    
    int val_start_idx = query.indexOf("(");
    int val_end_idx = query.lastIndexOf(")");
    String val = query.substring(val_start_idx + 1, val_end_idx);
    System.out.println(val);
    
    //컬럼 분리
    String[] cols = col.split(",");
    System.out.println(cols.length);
    String[] vals = val.split(",", cols.length);
    System.out.println(vals.length);
    
    System.out.println(Arrays.toString(vals));
        
  }
  
  public void value_parser(String value){
    
  }

}
